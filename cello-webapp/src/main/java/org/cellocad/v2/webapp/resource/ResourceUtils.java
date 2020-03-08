/**
 * Copyright (C) 2020 Boston University (BU)
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.cellocad.v2.webapp.resource;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.cellocad.v2.webapp.ApplicationUtils;
import org.cellocad.v2.webapp.common.Utils;
import org.cellocad.v2.webapp.specification.library.serialization.HeaderSerializationConstants;
import org.cellocad.v2.webapp.specification.library.serialization.LibrarySerializationConstants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Utils for application resources.
 *
 * @author Timothy Jones
 *
 * @date 2020-03-08
 *
 */
public class ResourceUtils {

    private static String getTargetDataResourcesDirectory() {
        String rtn = "";
        rtn = ApplicationUtils.getResourcesDirectory() + Utils.getFileSeparator() + "target_data";
        return rtn;
    }

    private static String getUserConstraintsFileResourcesDirectory() {
        String rtn = "";
        rtn = getTargetDataResourcesDirectory() + Utils.getFileSeparator() + "ucf";
        return rtn;
    }

    public static String getUserConstraintsFileMetaDataFile() {
        String rtn = "";
        rtn = getUserConstraintsFileResourcesDirectory() + Utils.getFileSeparator() + "metadata.json";
        return rtn;
    }

    private static void initUserConstraintsFileMetaDataFile() throws IOException {
        String str = getUserConstraintsFileMetaDataFile();
        Utils.createFileIfNonExistant(str);
        // write empty array
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arr = mapper.createArrayNode();
        Utils.writeToFile(arr.toString(), str);
    }

    public static void appendToUserConstraintsFileMetaDataFile(File resource, JsonNode header)
            throws JsonGenerationException, JsonMappingException, IOException {
        String filepath = getUserConstraintsFileMetaDataFile();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = null;
        try {
            node = (ArrayNode) mapper.readTree(new File(filepath));
        } catch (IOException e) {
            throw new RuntimeException("Error with file.");
        }
        ObjectNode obj = mapper.createObjectNode();
        obj.put("file", resource.getName());
        obj.set("header", header);
        node.add(obj);
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(filepath), node);
        node = (ArrayNode) mapper.readTree(new File(filepath));
    }

    private static void initTargetDataResources() throws IOException {
        Utils.createDirectoryIfNonExistant(getTargetDataResourcesDirectory());
        Utils.createDirectoryIfNonExistant(getUserConstraintsFileResourcesDirectory());
        initUserConstraintsFileMetaDataFile();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resources[] = resolver.getResources("classpath:/lib/files/v2/ucf/**/*.UCF.json");
        ObjectMapper mapper = new ObjectMapper();
        for (Resource r : resources) {
            File f = new File(getUserConstraintsFileResourcesDirectory() + Utils.getFileSeparator() + r.getFilename());
            FileUtils.copyInputStreamToFile(r.getInputStream(), f);
            JsonNode node = mapper.readTree(f);
            for (JsonNode collection : node) {
                if (collection.get(LibrarySerializationConstants.S_UCF_COLLECTION).asText()
                        .equals(HeaderSerializationConstants.S_UCF_COLLECTION)) {
                    appendToUserConstraintsFileMetaDataFile(f, collection);
                    break;
                }
            }
        }
    }

    public static void initResources() throws IOException {
        Utils.createDirectoryIfNonExistant(ApplicationUtils.getResourcesDirectory());
        initTargetDataResources();
    }

}