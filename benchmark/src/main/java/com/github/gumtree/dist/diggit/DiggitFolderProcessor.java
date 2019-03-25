/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016 Jean-Rémy Falleri <jr.falleri@gmail.com>
 */

package com.github.gumtree.dist.diggit;

import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DiggitFolderProcessor {

    private static String inputDir;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Wrong number of arguments");
            System.exit(-1);
        }

        inputDir = args[0];

        processFiles();
    }

    public static void processFiles() throws Exception {
        Files.walk(Paths.get(inputDir)).filter(Files::isRegularFile).forEach(file ->  {
            String name = file.toString();
            if (name.contains("/src/")) {
                String dst = name.replace("/src/", "/dst/");
                processFilePair(name, dst);
            }
        });
    }

    public static void processFilePair(String src, String dst) {
        System.out.println(src);
        TreeContext tcSrc = getTreeContext(src);
        new TreeValidator().validate(tcSrc);
        System.out.println(dst);
        TreeContext tcDst = getTreeContext(dst);
        new TreeValidator().validate(tcDst);
    }

    private static TreeContext getTreeContext(String file) {
        try {
            if (file.endsWith(".java"))
                return new JdtTreeGenerator().generateFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}