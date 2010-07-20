/**
 * This file is part of Maven Deployit plugin.
 *
 * Maven Deployit plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Maven Deployit plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Maven Deployit plugin.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.jar.Manifest
import java.util.jar.Attributes

def void checkIsFile(file) {
  assert file.exists(), " file " + file;
  assert file.isFile(), " file " + file;
}

def checkIsDir(File file) {
  assert file.exists(), " file " + file;
  assert file.isDirectory(), " file " + file;
}

def darDirectory = new File(basedir, "target/deployment-package/maven-deployit-plugin-test-victim-generate-dar-pom/1.0");
checkIsDir(darDirectory);
checkIsDir(new File(darDirectory, "ConfigurationFiles"));
checkIsDir(new File(darDirectory, "SqlFiles"));
checkIsFile(new File(darDirectory, "ear/PetClinic-1.0.ear"));
checkIsFile(new File(darDirectory, "META-INF/MANIFEST.MF"));

def manifest = new File(darDirectory, "META-INF/MANIFEST.MF");
manifest.eachLine {line ->
  println line
}

def m = new Manifest(manifest.newInputStream())

def entries = m.getEntries();
assert entries.size() == 3, "3 entries";
def earEntries = entries.findAll { key, value -> key == 'Ear/PetClinic-1.0.ear'};

assert earEntries.size() == 1
def earEntry = earEntries.iterator().next();
def attributes = earEntry.getValue()

assert attributes.containsKey(new Attributes.Name("CI-Type"));
assert attributes.containsKey(new Attributes.Name("CI-Name"));
assert attributes.get(new Attributes.Name("CI-Name")).equals("PetClinic")

return true;
