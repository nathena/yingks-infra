package com.yingks.infra.domain.store;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.yingks.infra.utils.LogHelper;

public class BeanPropertyRowMapper<T> implements RowMapper<T> {
	
    /** The class we are mapping to */
    private final Class<T> mappedClass;

    /** Map of the fields we provide mapping for */
    private Map<String, PropertyDescriptor> mappedFields;

    private final boolean checkColumns;

    private final boolean checkProperties;

    /** Set of bean properties we provide mapping for */
    private Set<String> mappedProperties;

    /**
     * Create a new BeanPropertyRowMapper, accepting unpopulated properties
     * in the target bean.
     *
     * @param mappedClass the class that each row should be mapped to
     */
    public BeanPropertyRowMapper(Class<T> mappedClass) {
        this(mappedClass,false,false);
    }
    
    public BeanPropertyRowMapper(Class<T> mappedClass, boolean checkProperties) {
        this(mappedClass,false,checkProperties);
    }
    
    public BeanPropertyRowMapper(Class<T> mappedClass, boolean checkColumns, boolean checkProperties) {
        this.mappedClass = mappedClass;
        this.checkProperties = checkProperties;
        this.checkColumns = checkColumns;
        initialize();
    }

    /**
     * Initialize the mapping metadata for the given class.
     *
     * @param mappedClass the mapped class.
     */
    protected void initialize() {
        this.mappedFields = new HashMap<String, PropertyDescriptor>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        if (checkProperties) {
            mappedProperties = new HashSet<String>();
        }
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            if (pd.getWriteMethod() != null) {
                if (checkProperties) {
                    this.mappedProperties.add(pd.getName());
                }
                this.mappedFields.put(pd.getName().toLowerCase(), pd);
                for (String underscoredName : underscoreName(pd.getName())) {
                    if (underscoredName != null
                            && !pd.getName().toLowerCase().equals(underscoredName)) {
                        this.mappedFields.put(underscoredName, pd);
                    }
                }
            }
        }
    }

    /**
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters are converted to lower case with a preceding
     * underscore.
     *
     * @param camelCaseName the string containing original name
     * @return the converted name
     */
    private String[] underscoreName(String camelCaseName) 
    {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        String name = result.toString();
        // 当name为user1_name2时，使name2为user_1_name_2
        // 这使得列user_1_name_2的列能映射到user1Name2属性
        String name2 = null;
        boolean digitFound = false;
        for (int i = name.length() - 1; i >= 0; i--) {
            if (Character.isDigit(name.charAt(i))) {
                // 遇到数字就做一个标识并continue,直到不是时才不continue
                digitFound = true;
                continue;
            }
            // 只有上一个字符是数字才做下划线
            if (digitFound && i < name.length() - 1 && i > 0) {
                if (name2 == null) {
                    name2 = name;
                }
                name2 = name2.substring(0, i + 1) + "_" + name2.substring(i + 1);
            }
            digitFound = false;
        }
        
        //尾巴是name_时 转为 name
        if(name!=null && name.endsWith("_"))
        {
        	name = name.substring(0,name.length()-1);
        }
        
        if(name2!=null && name2.endsWith("_"))
        {
        	name2 = name2.substring(0,name2.length()-1);
        }
        
        LogHelper.debug(mappedClass.getName()+" : "+camelCaseName+" => "+name+","+name2);
        
        return new String[] { name, name2 };
    }

    /**
     * Extract the values for all columns in the current row.
     * <p>
     * Utilizes public setters and result set metadata.
     *
     * @see java.sql.ResultSetMetaData
     */
    public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
        
        T mappedObject = instantiateClass(this.mappedClass);
        BeanWrapper bw = new BeanWrapperImpl(mappedObject);

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        Set<String> populatedProperties = (checkProperties ? new HashSet<String>() : null);

        for (int index = 1; index <= columnCount; index++) 
        {
            String column = JdbcUtils.lookupColumnName(rsmd, index).toLowerCase();
            PropertyDescriptor pd = this.mappedFields.get(column);
            if (pd != null) 
            {
                try 
                {
                    Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
                    if (rowNumber == 0) 
                    {
                        LogHelper.debug("Mapping column '" + column + "' to property '" + pd.getName()
                                + "' of type " + pd.getPropertyType());
                    }
                    
                    if(null == value && ( PrimitiveTypeChecked.checkNumberType(pd.getPropertyType()) || PrimitiveTypeChecked.checkFullObjectType(pd.getPropertyType()) ))
                    {
                    	bw.setPropertyValue(pd.getName(), 0);
                    }
                    else
                    {
                    	bw.setPropertyValue(pd.getName(), value);
                    }
                    
                    if (populatedProperties != null) 
                    {
                        populatedProperties.add(pd.getName());
                    }
                } 
                catch (NotWritablePropertyException ex) 
                {
                    throw new DataRetrievalFailureException("Unable to map column " + column
                            + " to property " + pd.getName(), ex);
                }
            } 
            else 
            {
                if (checkColumns) {
                    throw new InvalidDataAccessApiUsageException("Unable to map column '" + column
                            + "' to any properties of bean " + this.mappedClass.getName());
                }
                if (rowNumber == 0) {
                	LogHelper.debug("Unable to map column '" + column + "' to any properties of bean "
                            + this.mappedClass.getName());
                }
            }
        }

        if (populatedProperties != null && !populatedProperties.equals(this.mappedProperties)) {
            throw new InvalidDataAccessApiUsageException(
                    "Given ResultSet does not contain all fields "
                            + "necessary to populate object of class [" + this.mappedClass + "]: "
                            + this.mappedProperties);
        }

        return mappedObject;
    }

    /**
     *
     * @param clazz
     * @return
     * @throws BeanInstantiationException
     * @see {@link BeanUtils#instantiateClass(Class)}
     */
    private static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException 
    {
        try 
        {
            return clazz.newInstance();
        } 
        catch (Exception ex) 
        {
            throw new BeanInstantiationException(clazz, ex.getMessage(), ex);
        }
    }
}
