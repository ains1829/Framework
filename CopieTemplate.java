package copie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopieTemplate {
  public CopieTemplate() {
  }

  public void Copying(String nameProject, String project_path, String template) {
    String path = "./Template/" + template + "/";
    Path source = Paths.get(path);
    Path target = Paths.get(project_path + nameProject);
    try {
      Files.walk(source)
          .forEach(sourcePath -> {
            try {
              Path targetPath = target.resolve(source.relativize(sourcePath));
              Files.copy(sourcePath, targetPath);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
      Files.move(target, target.resolveSibling(nameProject));
      System.out.println("Copie et renommage terminés.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
