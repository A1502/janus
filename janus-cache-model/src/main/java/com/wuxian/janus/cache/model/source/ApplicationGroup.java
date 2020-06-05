package com.wuxian.janus.cache.model.source;

import lombok.Getter;

import java.util.*;

public class ApplicationGroup {

    @Getter
    private List<Application> applications = new ArrayList<>();

    @Getter
    private List<OuterObject> outerObjects = new ArrayList<>();

    @Getter
    private List<OuterObjectType> outerObjectTypes = new ArrayList<>();

    public ApplicationGroup addItem(OuterObjectType... outerObjectType) {
        this.outerObjectTypes.addAll(Arrays.asList(outerObjectType));
        return this;
    }

    public ApplicationGroup addItem(OuterObject... outerObject) {
        this.outerObjects.addAll(Arrays.asList(outerObject));
        return this;
    }

    public ApplicationGroup addItem(Application... application) {
        this.applications.addAll(Arrays.asList(application));
        return this;
    }
}
