package edu.ezip.ing1.pds.business.dto;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectUtils {

    /**
     * Populates fields of the given object from a ResultSet.
     *
     * @param obj        The object to populate.
     * @param resultSet  The ResultSet containing the data.
     * @param fieldNames The field names to map from ResultSet to object fields.
     * @throws NoSuchFieldException    If a field does not exist in the object.
     * @throws SQLException            If there is an error accessing the ResultSet.
     * @throws IllegalAccessException  If the field is not accessible.
     */
    public static <T> void setFieldsFromResultSet(final T obj, final ResultSet resultSet, final String... fieldNames)
            throws NoSuchFieldException, SQLException, IllegalAccessException {
        for (final String fieldName : fieldNames) {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true); // Ensure private fields can be accessed
            field.set(obj, resultSet.getObject(fieldName));
        }
    }

    /**
     * Builds a PreparedStatement with the given field values from the object.
     *
     * @param preparedStatement The PreparedStatement to populate.
     * @param obj               The object containing field values.
     * @param fieldNames        The names of the fields to extract values from.
     * @return The populated PreparedStatement.
     * @throws NoSuchFieldException    If a field does not exist in the object.
     * @throws SQLException            If there is an error setting the statement values.
     * @throws IllegalAccessException  If the field is not accessible.
     */
    public static <T> PreparedStatement buildPreparedStatement(final PreparedStatement preparedStatement, final T obj,
                                                               final String... fieldNames) throws NoSuchFieldException, SQLException, IllegalAccessException {
        int index = 0;
        for (final String fieldName : fieldNames) {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(obj);
            preparedStatement.setObject(++index, value); // Use setObject for more flexibility
        }
        return preparedStatement;
    }

    /**
     * Generates a string representation of the object based on its fields.
     *
     * @param obj The object to convert to a string.
     * @return A string representation of the object.
     */
    public static <T> String toString(final T obj) {
        StringBuilder result = new StringBuilder(obj.getClass().getSimpleName() + "{");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                result.append(field.getName())
                        .append("='")
                        .append(field.get(obj))
                        .append("', ");
            } catch (IllegalAccessException e) {
                result.append(field.getName()).append("='[access denied]', ");
            }
        }
        // Remove the trailing comma and space, if present
        if (result.length() > 2) {
            result.setLength(result.length() - 2);
        }
        result.append("}");
        return result.toString();
    }
}
