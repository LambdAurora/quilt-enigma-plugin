/*
 * Copyright 2022 QuiltMC
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

package org.quiltmc.enigmaplugin.index;

import cuchaz.enigma.translation.representation.TypeDescriptor;
import cuchaz.enigma.translation.representation.entry.ClassEntry;
import cuchaz.enigma.translation.representation.entry.FieldEntry;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.quiltmc.enigmaplugin.util.AsmUtil;

import java.util.HashSet;
import java.util.Set;

public class LoggerIndex implements Opcodes {
    private static final String LOGGER_TYPE = "Lorg/slf4j/Logger;";

    private final Set<FieldEntry> fields = new HashSet<>();

    public void visitClassNode(ClassNode parent) {
        var parentEntry = new ClassEntry(parent.name);

        for (var field : parent.fields) {
            if (AsmUtil.matchAccess(field, ACC_STATIC, ACC_FINAL)) {
                if (field.desc.equals(LOGGER_TYPE)) {
                    var fieldEntry = new FieldEntry(parentEntry, field.name, new TypeDescriptor(field.desc));

                    this.fields.add(fieldEntry);
                }
            }
        }
    }

    public boolean hasField(FieldEntry field) {
        return this.fields.contains(field);
    }
}
