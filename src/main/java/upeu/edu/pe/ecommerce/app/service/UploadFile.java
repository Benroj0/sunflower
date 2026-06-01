package upeu.edu.pe.ecommerce.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.multipart.MultipartFile;

public class UploadFile {
    private final String FOLDER = "images";
    private final String IMG_DEFAULT = "default.jpg";
    
    public String upload(MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            byte[] bytes = multipartFile.getBytes();
            
            // Obtener la ruta absoluta de la carpeta 'images'
            Path folderPath = Paths.get(FOLDER).toAbsolutePath();
            
            // Si la carpeta no existe por alguna razón del IDE, la crea automáticamente
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            
            // Armar la ruta completa del archivo
            Path path = folderPath.resolve(multipartFile.getOriginalFilename());
            
            // Escribir el archivo en el disco
            Files.write(path, bytes);
            return multipartFile.getOriginalFilename();
        }
        return IMG_DEFAULT;
    }
    
    public void delete(String nameFile) {
        Path path = Paths.get(FOLDER).toAbsolutePath().resolve(nameFile);
        File file = path.toFile();
        if (file.exists()) {
            file.delete();
        }
    }
}