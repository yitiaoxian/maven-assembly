package myproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationFileAttachment;

/**
 * This is an example on how to extract all embedded files from a PDF document.
 *
 */
public final class ExtractEmbeddedFiles{
    private ExtractEmbeddedFiles()
    {
    }

    /**
     * This is the main method.
     *
     * @param args The command line arguments.
     *
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException
    {
        PDDocument document = null;
        try
        {
            File pdfFile = new File("E:\\tika-test-dir\\test\\wps嵌套\\wps的pdf（压缩和图片）.pdf");
            String filePath = pdfFile.getParent() + System.getProperty("file.separator");
            document = PDDocument.load(new File("E:\\tika-test-dir\\test\\wps嵌套\\wps的pdf（压缩和图片）.pdf"));
            PDDocumentNameDictionary namesDictionary =
                    new PDDocumentNameDictionary( document.getDocumentCatalog() );

            PDEmbeddedFilesNameTreeNode efTree = namesDictionary.getEmbeddedFiles();
            if (efTree != null)
            {
                Map<String, PDComplexFileSpecification> names = efTree.getNames();
                if (names != null)
                {
                    extractFiles(names, filePath);
                }
                else
                {
                    List<PDNameTreeNode<PDComplexFileSpecification>> kids = efTree.getKids();
                    for (PDNameTreeNode<PDComplexFileSpecification> node : kids)
                    {
                        names = node.getNames();
                        extractFiles(names, filePath);
                    }
                }
            }

            // extract files from annotations
            for (PDPage page : document.getPages())
            {
                for (PDAnnotation annotation : page.getAnnotations())
                {
                    if (annotation instanceof PDAnnotationFileAttachment)
                    {
                        PDAnnotationFileAttachment annotationFileAttachment = (PDAnnotationFileAttachment) annotation;
                        PDComplexFileSpecification fileSpec = (PDComplexFileSpecification) annotationFileAttachment.getFile();
                        PDEmbeddedFile embeddedFile = getEmbeddedFile(fileSpec);
                        extractFile(filePath, fileSpec.getFilename(), embeddedFile);
                    }
                }
            }

        }
        finally
        {
            if( document != null )
            {
                document.close();
            }
        }
    }

    private static void extractFiles(Map<String, PDComplexFileSpecification> names, String filePath)
            throws IOException
    {
        for (Entry<String, PDComplexFileSpecification> entry : names.entrySet())
        {
            String filename = entry.getKey();
            PDComplexFileSpecification fileSpec = entry.getValue();
            PDEmbeddedFile embeddedFile = getEmbeddedFile(fileSpec);
            extractFile(filePath, filename, embeddedFile);
        }
    }

    private static void extractFile(String filePath, String filename, PDEmbeddedFile embeddedFile)
            throws IOException
    {
        String embeddedFilename = filePath + filename;
        File file = new File("E:\\tika-test-dir\\test\\wps嵌套\\"+filename);
        System.out.println("Writing " + embeddedFilename);
        try (FileOutputStream fos = new FileOutputStream(file))
        {
            fos.write(embeddedFile.toByteArray());
        }
    }

    private static PDEmbeddedFile getEmbeddedFile(PDComplexFileSpecification fileSpec )
    {
        // search for the first available alternative of the embedded file
        PDEmbeddedFile embeddedFile = null;
        if (fileSpec != null)
        {
            embeddedFile = fileSpec.getEmbeddedFileUnicode();
            if (embeddedFile == null)
            {
                embeddedFile = fileSpec.getEmbeddedFileDos();
            }
            if (embeddedFile == null)
            {
                embeddedFile = fileSpec.getEmbeddedFileMac();
            }
            if (embeddedFile == null)
            {
                embeddedFile = fileSpec.getEmbeddedFileUnix();
            }
            if (embeddedFile == null)
            {
                embeddedFile = fileSpec.getEmbeddedFile();
            }
        }
        return embeddedFile;
    }
}