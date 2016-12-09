package mit.spbau.ru;
// This class is a part of the 4th "sprint" of the database labs
// running in Saint-Petersburg Academic University in Fall'16
//
// Author: Dmitry Barashev
// License: WTFPL

import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldConverter;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is just a dirty hack which overrides a single method #isNestedSavePointsSupported
 * and returns false. This makes transaction isolation levels working with PostgreSQL.
 */
public class ProxyDatabaseType implements DatabaseType {
    DatabaseType myDelegate;

    public ProxyDatabaseType(DatabaseType databaseType) {
        myDelegate = databaseType;
    }

    public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
        return myDelegate.isDatabaseUrlThisType(url, dbTypePart);
    }

    public void loadDriver() throws SQLException {
        myDelegate.loadDriver();
    }

    public void setDriver(Driver driver) {
        myDelegate.setDriver(driver);
    }

    public void appendColumnArg(String tableName, StringBuilder sb, FieldType fieldType, List<String> additionalArgs, List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) throws SQLException {
        myDelegate.appendColumnArg(tableName, sb, fieldType, additionalArgs, statementsBefore, statementsAfter, queriesAfter);
    }

    public void addPrimaryKeySql(FieldType[] fieldTypes, List<String> additionalArgs, List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) throws SQLException {
        myDelegate.addPrimaryKeySql(fieldTypes, additionalArgs, statementsBefore, statementsAfter, queriesAfter);
    }

    public void addUniqueComboSql(FieldType[] fieldTypes, List<String> additionalArgs, List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) throws SQLException {
        myDelegate.addUniqueComboSql(fieldTypes, additionalArgs, statementsBefore, statementsAfter, queriesAfter);
    }

    public void dropColumnArg(FieldType fieldType, List<String> statementsBefore, List<String> statementsAfter) {
        myDelegate.dropColumnArg(fieldType, statementsBefore, statementsAfter);
    }

    public void appendEscapedEntityName(StringBuilder sb, String word) {
        myDelegate.appendEscapedEntityName(sb, word);
    }

    public void appendEscapedWord(StringBuilder sb, String word) {
        myDelegate.appendEscapedWord(sb, word);
    }

    public String generateIdSequenceName(String tableName, FieldType idFieldType) {
        return myDelegate.generateIdSequenceName(tableName, idFieldType);
    }

    public String getCommentLinePrefix() {
        return myDelegate.getCommentLinePrefix();
    }

    public boolean isIdSequenceNeeded() {
        return myDelegate.isIdSequenceNeeded();
    }

    public DataPersister getDataPersister(DataPersister defaultPersister, FieldType fieldType) {
        return myDelegate.getDataPersister(defaultPersister, fieldType);
    }

    public FieldConverter getFieldConverter(DataPersister dataType, FieldType fieldType) {
        return myDelegate.getFieldConverter(dataType, fieldType);
    }

    public boolean isVarcharFieldWidthSupported() {
        return myDelegate.isVarcharFieldWidthSupported();
    }

    public boolean isLimitSqlSupported() {
        return myDelegate.isLimitSqlSupported();
    }

    public boolean isLimitAfterSelect() {
        return myDelegate.isLimitAfterSelect();
    }

    public void appendLimitValue(StringBuilder sb, long limit, Long offset) {
        myDelegate.appendLimitValue(sb, limit, offset);
    }

    public boolean isOffsetSqlSupported() {
        return myDelegate.isOffsetSqlSupported();
    }

    public boolean isOffsetLimitArgument() {
        return myDelegate.isOffsetLimitArgument();
    }

    public void appendOffsetValue(StringBuilder sb, long offset) {
        myDelegate.appendOffsetValue(sb, offset);
    }

    public void appendSelectNextValFromSequence(StringBuilder sb, String sequenceName) {
        myDelegate.appendSelectNextValFromSequence(sb, sequenceName);
    }

    public void appendCreateTableSuffix(StringBuilder sb) {
        myDelegate.appendCreateTableSuffix(sb);
    }

    public boolean isCreateTableReturnsZero() {
        return myDelegate.isCreateTableReturnsZero();
    }

    public boolean isCreateTableReturnsNegative() {
        return myDelegate.isCreateTableReturnsNegative();
    }

    public boolean isEntityNamesMustBeUpCase() {
        return myDelegate.isEntityNamesMustBeUpCase();
    }

    public String upCaseEntityName(String entityName) {
        return myDelegate.upCaseEntityName(entityName);
    }

    public boolean isNestedSavePointsSupported() {
        return false;
    }

    public String getPingStatement() {
        return myDelegate.getPingStatement();
    }

    public boolean isBatchUseTransaction() {
        return myDelegate.isBatchUseTransaction();
    }

    public boolean isTruncateSupported() {
        return myDelegate.isTruncateSupported();
    }

    public boolean isCreateIfNotExistsSupported() {
        return myDelegate.isCreateIfNotExistsSupported();
    }

    public boolean isCreateIndexIfNotExistsSupported() {
        return myDelegate.isCreateIndexIfNotExistsSupported();
    }

    public boolean isSelectSequenceBeforeInsert() {
        return myDelegate.isSelectSequenceBeforeInsert();
    }

    public boolean isAllowGeneratedIdInsertSupported() {
        return myDelegate.isAllowGeneratedIdInsertSupported();
    }

    public String getDatabaseName() {
        return myDelegate.getDatabaseName();
    }

    public <T> DatabaseTableConfig<T> extractDatabaseTableConfig(ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
        return myDelegate.extractDatabaseTableConfig(connectionSource, clazz);
    }

    public void appendInsertNoColumns(StringBuilder sb) {
        myDelegate.appendInsertNoColumns(sb);
    }
}
