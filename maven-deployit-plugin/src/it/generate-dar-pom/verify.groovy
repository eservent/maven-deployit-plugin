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


println "end check"

return true;
