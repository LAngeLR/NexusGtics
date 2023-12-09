package com.example.nexusgtics.controllers;

import com.example.nexusgtics.entity.Archivo;
import com.example.nexusgtics.entity.Archivossitio;
import com.example.nexusgtics.repository.ArchivoRepository;
import com.example.nexusgtics.repository.ArchivoSitioRepository;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

@RestController
public class GcsController {

    private final ArchivoRepository archivoRepository;
    private final ArchivoSitioRepository archivoSitioRepository;
    public GcsController(ArchivoRepository archivoRepository, ArchivoSitioRepository archivoSitioRepository) {
        this.archivoRepository = archivoRepository;
        this.archivoSitioRepository = archivoSitioRepository;
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

    public static byte[] downloadObjectArchivo
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


    /* Subir la imagen al repositorio (ARCHIVO) */
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

    /* PARA SUBIR ARCHIVOS EN GENERAL */
    public static void uploadObjectArchivo
    (Archivossitio archivossitio) {
        try {
            Storage storage = StorageOptions.newBuilder().setProjectId("labgcp-401300").build().getService();
            Bucket bucket = storage.get("proyecto-archivos-gtics", Storage.BucketGetOption.fields());
//            RandomString id = new RandomString(6, ThreadLocalRandom.current());
            Blob blob = bucket.create(archivossitio.getNombreArchivo(), archivossitio.getArchivo(),archivossitio.getContentType());

            if (blob != null) {
                System.out.println("Se guardo exitosamente");

            }
        } catch (Exception e) {
//            LOGGER.error("An error occurred while uploading data. Exception: ", e);
            throw new RuntimeException("An error occurred while storing data to GCS");
        }
    }




    /* Obtener un archivo del Storage */
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
//                    System.out.println(archivo1.getIdArchivos());
//                    System.out.println(archivo1.getNombre());
                    String nombreArchivo = "archivo-"+archivo1.getIdArchivos();
                    System.out.println("NombreArchivoFOTO: " + nombreArchivo);
                    System.out.println("NombreArchivoByteFFOTO: " + archivo1.getNombre());
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




    /* Obtener un archivo del Storage */
    @GetMapping("/fileArchivo/{idSitio}/{idArchivo}")
    public ResponseEntity<byte[]> displayItemArchivo(@PathVariable("idSitio") String idSitioStr,
                                                     @PathVariable("idArchivo") String idArchivoStr) throws IOException {
        try {
            int idSitio = Integer.parseInt(idSitioStr);
            int idArchivo = Integer.parseInt(idArchivoStr);
            System.out.println("Id sitio: " + idSitio);
            System.out.println("Id Archivo: "+ idArchivo);
            if (idSitio <= 0 || idArchivo <= 0 || !archivoSitioRepository.existsById(idArchivo)) {
                HttpHeaders httpHeaders = new HttpHeaders();
                return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
            }else {
                Optional<Archivossitio> optionalArchivossitio = archivoSitioRepository.findById(idArchivo);
                if(optionalArchivossitio.isPresent()){
                    Archivossitio archivossitio = optionalArchivossitio.get();
//                    System.out.println(archivo1.getIdArchivos());
//                    System.out.println(archivo1.getNombre());
                    String nombreArchivo = "archivo-"+archivossitio.getIdSitioSitio().getIdSitios()+"-"+archivossitio.getId();
                    System.out.println("NombreArchivo: " + nombreArchivo);
                    System.out.println("NombreArchivoByte: " + archivossitio.getNombreArchivo());
                    byte[] image = downloadObjectArchivo("labgcp-401300", "proyecto-archivos-gtics", archivossitio.getNombreArchivo());
                    System.out.println("NombreArchivoByte: " + archivossitio.getNombreArchivo());


                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(archivossitio.getContentType()));
                    headers.setContentDisposition(ContentDisposition.builder("attachment").filename(archivossitio.getNombreArchivo()).build());

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


    @GetMapping("/siteDefault")
    public ResponseEntity<byte[]> DefaultImageSite() throws IOException {
        try{
            byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "siteDefault.png");
            HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE));
            return new ResponseEntity<>(image, headers, HttpStatus.OK);

        }catch (NumberFormatException e){
            HttpHeaders httpHeaders = new HttpHeaders();
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.NOT_FOUND);
        }

    }


    @GetMapping("/deviceDefault")
    public ResponseEntity<byte[]> DefaultImageDevice() throws IOException {
        try{
            byte[] image = downloadObject("labgcp-401300", "proyecto-gtics", "deviceDefault.png");
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