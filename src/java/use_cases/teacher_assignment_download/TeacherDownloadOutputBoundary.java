package use_cases.teacher_assignment_download;

import entities.PDFFile;

public interface TeacherDownloadOutputBoundary {
    void presentSuccess(String message, PDFFile pdffile);
    void presentError(String message);
}
