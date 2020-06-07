package com.wuxian.janus.cache.model.source;

import com.wuxian.janus.cache.model.ErrorFactory;
import com.wuxian.janus.cache.model.source.item.ApplicationItem;
import com.wuxian.janus.core.basis.StrictUtils;
import com.wuxian.janus.core.critical.DimensionEnum;
import com.wuxian.janus.core.critical.NativePermissionTemplateEnum;
import com.wuxian.janus.struct.layer2.PermissionTemplateStruct;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

public class PermissionTemplate extends CodeModel<PermissionTemplateStruct> implements ApplicationItem {

    @Getter
    @Setter
    private String outerObjectTypeCode;

    @Getter
    @Setter
    private Boolean multiple;

    //---------------------------------------------------------------------------------------------------------------------------------

    protected PermissionTemplate() {
        this.multiple = false;
    }

    /**
     * byId的静态构造方式，id不能为null
     */
    public static PermissionTemplate byId(String id) {
        if (id == null) {
            throw ErrorFactory.createIdCannotBeNullError();
        }
        PermissionTemplate result = new PermissionTemplate();
        result.id = id;
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    /**
     * code作为构造函数参数的时永远不能为null
     */
    public PermissionTemplate(String code) {
        super(code);
        this.multiple = false;
    }

    public static PermissionTemplate byId(String id, String code) {
        PermissionTemplate result = byId(id);
        result.code = code;
        return result;
    }

    public static PermissionTemplate byStruct(PermissionTemplateStruct struct) {
        PermissionTemplate result = new PermissionTemplate(struct.getCode());
        result.setStruct(struct);
        result.setMultiple(struct.getMultiple() != null && struct.getMultiple());
        if (struct.getId() != null) {
            result.setId(struct.getId().toString());
        }
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public PermissionTemplate(NativePermissionTemplateEnum code) {
        this(code == null ? null : code.getCode());
        this.multiple = (code != null && code.getDimension() == DimensionEnum.TENANT);
    }

    public static PermissionTemplate byId(String id, NativePermissionTemplateEnum code) {
        PermissionTemplate result = byId(id);
        result.code = code == null ? null : code.getCode();
        result.multiple = (code != null && code.getDimension() == DimensionEnum.TENANT);
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    public PermissionTemplate(String code, String outerObjectTypeCode) {
        this(code);
        validateCodeAndOuterObjectTypeCode(code, outerObjectTypeCode);
        this.outerObjectTypeCode = outerObjectTypeCode;
        this.multiple = (outerObjectTypeCode != null);
    }

    public static PermissionTemplate byId(String id, String code, String outerObjectTypeCode) {
        PermissionTemplate result = byId(id);
        validateCodeAndOuterObjectTypeCode(code, outerObjectTypeCode);
        result.code = code;
        result.outerObjectTypeCode = outerObjectTypeCode;
        result.multiple = (outerObjectTypeCode != null);
        return result;
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    private static void validateCodeAndOuterObjectTypeCode(String code, String outerObjectTypeCode) {
        NativePermissionTemplateEnum nativeCode = NativePermissionTemplateEnum.getByCode(code);
        if (nativeCode != null && outerObjectTypeCode != null) {
            throw ErrorFactory.createInvalidNativePermissionTemplateCodeError(code, outerObjectTypeCode);
        }
    }

    @Override
    protected Class<PermissionTemplateStruct> getStructClass() {
        return PermissionTemplateStruct.class;
    }

    @Override
    protected <T extends BaseModel> Class<T> getThisClass() {
        return (Class<T>) this.getClass();
    }

    @Override
    public PermissionTemplateStruct buildStruct(Function<BaseModel<PermissionTemplateStruct>, PermissionTemplateStruct> otherFieldBuilder) {
        PermissionTemplateStruct struct = super.buildStruct(otherFieldBuilder);
        struct.setId(this.buildIdType().getValue());
        struct.setCode(this.getCode());
        struct.setMultiple(this.getMultiple());
        return struct;
    }

    @Override
    public boolean keyFieldsEquals(BaseModel<PermissionTemplateStruct> other) {
        if (other instanceof PermissionTemplate) {
            PermissionTemplate otherModel = (PermissionTemplate) other;
            return StrictUtils.equals(this.code, otherModel.code);
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }

    @Override
    public void fillKeyFields(BaseModel<PermissionTemplateStruct> other) {
        if (other instanceof PermissionTemplate) {
            PermissionTemplate otherModel = (PermissionTemplate) other;
            this.code = otherModel.code;
        } else {
            throw ErrorFactory.createInvalidModelClassError(other, this);
        }
    }
}
