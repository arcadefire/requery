/*
 * Copyright 2016 requery.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.requery.meta;

import io.requery.proxy.EntityProxy;
import io.requery.query.ExpressionType;
import io.requery.util.Objects;
import io.requery.util.function.Function;
import io.requery.util.function.Supplier;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

abstract class BaseType<T> implements Type<T> {

    Class<T> type;
    Class<? super T> baseType;
    String name;
    boolean cacheable;
    boolean stateless;
    boolean readOnly;
    boolean immutable;
    Set<Attribute<T, ?>> attributes;
    Set<QueryExpression<?>> expressions;
    Supplier<T> factory;
    Function<T, EntityProxy<T>> proxyProvider;
    Set<Class<?>> referencedTypes;
    String[] tableCreateAttributes;
    Supplier<?> builderFactory;
    Function<?, T> buildFunction;

    private Set<Attribute<T, ?>> keyAttributes;
    private Attribute<T, ?> keyAttribute;

    BaseType() {
        cacheable = true;
        referencedTypes = new LinkedHashSet<>();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<T> classType() {
        return type;
    }

    @Override
    public Class<? super T> baseType() {
        return baseType;
    }

    @Override
    public ExpressionType type() {
        return ExpressionType.NAME;
    }

    @Override
    public boolean isCacheable() {
        return cacheable;
    }

    @Override
    public boolean isImmutable() {
        return immutable;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean isStateless() {
        return stateless;
    }

    @Override
    public boolean isBuildable() {
        return builderFactory != null;
    }

    @Override
    public Set<Attribute<T, ?>> attributes() {
        return attributes;
    }

    @Override
    public Set<Attribute<T, ?>> keyAttributes() {
        if (keyAttributes == null) {
            keyAttributes = new LinkedHashSet<>();
            for (Attribute<T, ?> attribute : attributes) {
                if (attribute.isKey()) {
                    keyAttributes.add(attribute);
                }
            }
            keyAttributes = Collections.unmodifiableSet(keyAttributes);
            if (keyAttributes.size() == 1) {
                keyAttribute = keyAttributes.iterator().next();
            }
        }
        return keyAttributes;
    }

    @Override
    public Attribute<T, ?> singleKeyAttribute() {
        return keyAttribute;
    }

    @Override
    public <B> Supplier<B> builderFactory() {
        @SuppressWarnings("unchecked")
        Supplier<B> supplier = (Supplier<B>) builderFactory;
        return supplier;
    }

    @Override
    public <B> Function<B, T> buildFunction() {
        @SuppressWarnings("unchecked")
        Function<B, T> function = (Function<B, T>) buildFunction;
        return function;
    }

    @Override
    public Supplier<T> factory() {
        return factory;
    }

    @Override
    public Function<T, EntityProxy<T>> proxyProvider() {
        return proxyProvider;
    }

    @Override
    public String[] tableCreateAttributes() {
        return tableCreateAttributes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Type) {
            Type other = (Type) obj;
            return Objects.equals(classType(), other.classType()) &&
                   Objects.equals(name(), other.name());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "classType: " + type.toString() +
            " name: " + name +
            " readonly: " + readOnly +
            " immutable: " + immutable +
            " stateless: " + stateless +
            " cacheable: " + cacheable;
    }
}
