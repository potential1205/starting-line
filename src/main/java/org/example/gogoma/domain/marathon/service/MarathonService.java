package org.example.gogoma.domain.marathon.service;

import org.example.gogoma.controller.request.CreateMarathonRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MarathonService {
    void createMarathon(CreateMarathonRequest createMarathonRequest,
                        MultipartFile thumbnailFile,
                        MultipartFile infoImageFile,
                        MultipartFile courseImageFile);
}
