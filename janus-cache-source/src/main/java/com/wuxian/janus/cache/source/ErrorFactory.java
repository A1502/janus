package com.wuxian.janus.cache.source;

import com.wuxian.janus.cache.source.model.BaseModel;
import com.wuxian.janus.core.basis.ErrorCodeException;
import com.wuxian.janus.core.critical.CoverageTypeEnum;

import java.lang.reflect.Type;

public final class ErrorFactory {

    private static final String PRE = "JANUS_1:";

    public final static String PERMISSION_TEMPLATE_CODE_CANNOT_BE_NULL = PRE + "PERMISSION_TEMPLATE_CODE_CANNOT_BE_NULL";

    public final static String CODE_CANNOT_BE_NULL = PRE + "CODE_CANNOT_BE_NULL";

    public final static String ID_CANNOT_BE_NULL = PRE + "ID_CANNOT_BE_NULL";

    public final static String ILLEGAL_ITEM_TYPE = PRE + "ILLEGAL_ITEM_TYPE";

    public final static String ILLEGAL_NATIVE_COVERAGE = PRE + "ILLEGAL_NATIVE_COVERAGE";

    public final static String ID_GENERATOR_FACTORY_NOT_SUPPORT = PRE + "ID_GENERATOR_FACTORY_NOT_SUPPORT";

    public final static String ENTITY_MERGING_FAILED = PRE + "ENTITY_MERGING_FAILED";

    public final static String ID_CONFLICT = PRE + "ID_CONFLICT";

    public final static String KEY_FIELDS_CONFLICT = PRE + "KEY_FIELDS_CONFLICT";

    public final static String DUPLICATED_PROPERTY = PRE + "DUPLICATED_PROPERTY";

    public final static String BUILD_ENTITY_FAILED = PRE + "BUILD_ENTITY_FAILED";

    public final static String NOTHING_FOUND = PRE + "NOTHING_FOUND";

    public final static String LIST_ENTITY_FIELD = PRE + "LIST_ENTITY_FIELD";

    public final static String OUTER_OBJECT_TYPE_CODE_CANNOT_BE_NULL = PRE + "OUTER_OBJECT_TYPE_CODE_CANNOT_BE_NULL";

    public final static String OUTER_OBJECT_CODE_CANNOT_BE_NULL = PRE + "OUTER_OBJECT_CODE_CANNOT_BE_NULL";

    public final static String INVALID_MODEL_CLASS = PRE + "INVALID_MODEL_CLASS";

    public final static String KEY_FIELDS_NO_VALUE = PRE + "KEY_FIELDS_NO_VALUE";

    public final static String OUTER_OBJECT_TYPE_NOT_MATCH = PRE + "OUTER_OBJECT_TYPE_NOT_MATCH";

    public static ErrorCodeException createPermissionTemplateCodeCannotBeNullError() {
        String message = "PermissionTemplateCode不能为null";
        return new ErrorCodeException(PERMISSION_TEMPLATE_CODE_CANNOT_BE_NULL, message, null);
    }

    public static ErrorCodeException createCodeCannotBeNullError() {
        String message = "Code不能为null";
        return new ErrorCodeException(CODE_CANNOT_BE_NULL, message, null);
    }

    public static ErrorCodeException createIdCannotBeNullError() {
        String message = "Id不能为null";
        return new ErrorCodeException(ID_CANNOT_BE_NULL, message, null);
    }

    public static ErrorCodeException createIllegalItemTypeError(String type) {
        String message = "Item是非法类型:" + type;
        return new ErrorCodeException(ILLEGAL_ITEM_TYPE, message, null);
    }

    public static ErrorCodeException createIllegalNativeCoverageError(CoverageTypeEnum coverage, String nativeCode) {
        String message = "在设定coverage = " + coverage + "时，不能使用" + nativeCode;
        return new ErrorCodeException(ILLEGAL_NATIVE_COVERAGE, message, null);
    }

    public static ErrorCodeException createIdGeneratorFactoryNotSupportError(Class<?> factoryClass, Type type) {
        String message = factoryClass.getName() + "不支持Value类型为" + type.toString() + "的IdType";
        return new ErrorCodeException(ID_GENERATOR_FACTORY_NOT_SUPPORT, message, null);
    }

    public static ErrorCodeException createEntityMergingFailedError(String object, String detail) {
        String message = "合并entity发生错误:" + object;
        return new ErrorCodeException(ENTITY_MERGING_FAILED, message, detail);
    }

    public static ErrorCodeException createIdConflictError(String object) {
        String message = "数据存在多个Id的冲突:" + object;
        return new ErrorCodeException(ID_CONFLICT, message, null);
    }

    public static ErrorCodeException createKeyFieldsConflictError(String object) {
        String message = "存在Id相同但关键字段内容的冲突的实例:" + object;
        return new ErrorCodeException(KEY_FIELDS_CONFLICT, message, null);
    }

    public static ErrorCodeException createKeyFieldsNoValueError(String object) {
        String message = "关键字段没有值:" + object;
        return new ErrorCodeException(KEY_FIELDS_NO_VALUE, message, null);
    }

    public static ErrorCodeException createDuplicatedFieldError(String object, String fieldName) {
        String message = "重复定义属性" + fieldName + "无法合并:" + object;
        return new ErrorCodeException(DUPLICATED_PROPERTY, message, null);
    }

    public static ErrorCodeException createBuildEntityFailedError(String object, String detail) {
        String message = "生成entity发生错误:" + object;
        return new ErrorCodeException(BUILD_ENTITY_FAILED, message, detail);
    }

    public static ErrorCodeException createNothingFoundError(String targetDesc, String findByDesc, String context) {
        String message = "按'" + findByDesc + "'查找" + targetDesc + "失败,错误相关上下文是:" + context;
        return new ErrorCodeException(NOTHING_FOUND, message, null);
    }

    public static ErrorCodeException createListEntityFieldError(String object, String fieldName) {
        String message = "List类型属性" + fieldName + "合并出错 : " + object;
        return new ErrorCodeException(LIST_ENTITY_FIELD, message, null);
    }

    public static ErrorCodeException createOuterObjectTypeCodeCannotBeNullError() {
        String message = "outerObjectTypeCode不能为null";
        return new ErrorCodeException(OUTER_OBJECT_TYPE_CODE_CANNOT_BE_NULL, message, null);
    }

    public static ErrorCodeException createOuterObjectCodeCannotBeNullError() {
        String message = "outerObjectType不能为null";
        return new ErrorCodeException(OUTER_OBJECT_CODE_CANNOT_BE_NULL, message, null);
    }

    public static ErrorCodeException createInvalidModelClassError(BaseModel actual, BaseModel expected) {
        String message;
        if (actual == null) {
            message = "实际输入为null,期望对象的类型为:" + expected.getClass().getTypeName();
        } else {
            message = "无效类型:" + actual.getClass().getTypeName()
                    + ",期望对象的类型为:" + expected.getClass().getTypeName();
        }
        return new ErrorCodeException(INVALID_MODEL_CLASS, message, null);
    }

    public static ErrorCodeException createOuterObjectTypeNotMatchError(String permissionTemplateDesc
            , String permissionDesc) {
        String message = "permissionTemplate:" + permissionTemplateDesc + "和permission:" + permissionDesc + " 的outerObjectType不匹配";
        return new ErrorCodeException(OUTER_OBJECT_TYPE_NOT_MATCH, message, null);
    }
}
