package com.example.nexusgtics.controllers;

import ch.qos.logback.core.model.Model;
import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Usuario;
import com.example.nexusgtics.repository.ArchivoRepository;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

@RestController
public class GcsController {

    private final ArchivoRepository archivoRepository;

    public GcsController(ArchivoRepository archivoRepository) {
        this.archivoRepository = archivoRepository;
    }

    /* Descargar la imagen del repositorio */
    public static byte[] downloadObject
            (String projectId, String bucketName, String blobName) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, blobName);

        try (ReadChannel reader = storage.reader(blobId)) {
            ByteBuffer bytes = ByteBuffer.allocate(64 * 64 * 1024);
            while (reader.read(bytes) > 0) {
                bytes.flip();
                bytes.clear();
            }
            byte[] image = bytes.array();
            return image;
        }

    }

    /* Subir la imagen al repositorio */
    public static void uploadObject
            (Archivo archivo) {
        try {
            Storage storage = StorageOptions.newBuilder().setProjectId("labgcp-401300").build().getService();
            Bucket bucket = storage.get("proyecto-gtics", Storage.BucketGetOption.fields());
//            RandomString id = new RandomString(6, ThreadLocalRandom.current());
            Blob blob = bucket.create(archivo.getNombre(), archivo.getArchivo(),archivo.getContentType());

            if (blob != null) {
                System.out.println("Se guardo exitosamente");
//                LOGGER.debug("File successfully uploaded to GCS");
//                return new FileDto(blob.getName(), blob.getMediaLink());
            }
        } catch (Exception e) {
//            LOGGER.error("An error occurred while uploading data. Exception: ", e);
            throw new RuntimeException("An error occurred while storing data to GCS");
        }
    }



    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> displayItemImage(@PathVariable("id") String idStr) throws IOException {
        try{
            int id = Integer.parseInt(idStr);
            if (id <= 0 || !archivoRepository.existsById(id)) {
                HttpHeaders httpHeaders = new HttpHeaders();
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
            }else {
                Optional<Archivo> optArchivo = archivoRepository.findById(id);
                if(optArchivo.isPresent()){
                    Archivo archivo1 = optArchivo.get();
                    System.out.println(archivo1.getIdArchivos());
                    System.out.println(archivo1.getNombre());
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos();
                    byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", archivo1.getNombre());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(archivo1.getContentType()));
                    //headers.setContentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE));

                    return new ResponseEntity<>(image, headers, HttpStatus.OK);
                } else {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
                }

            }
        }catch (NumberFormatException e){
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }

    }




    @GetMapping("/default")
    public ResponseEntity<byte[]> DefaultImage() throws IOException {
        try{
            byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "imagen.jpeg");
            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE));
            return new ResponseEntity<>(image, headers, HttpStatus.OK);

        }catch (NumberFormatException e){
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/userDefault")
    public ResponseEntity<byte[]> DefaultImageUser() throws IOException {
        try{
            byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "userDefault.png");
            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE));
            return new ResponseEntity<>(image, headers, HttpStatus.OK);

        }catch (NumberFormatException e){
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }

    }


}