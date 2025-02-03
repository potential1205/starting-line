package org.example.gogoma.domain.marathon.service;

import org.example.gogoma.controller.request.CreateMarathonRequest;
import org.example.gogoma.controller.request.MarathonDetailRequest;
import org.example.gogoma.controller.request.MarathonSearchRequest;
import org.example.gogoma.controller.response.MarathonDetailResponse;
import org.example.gogoma.controller.response.MarathonSearchResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MarathonService {
    void createMarathon(CreateMarathonRequest createMarathonRequest,
                        MultipartFile thumbnailFile,
                        MultipartFile infoImageFile,
                        MultipartFile courseImageFile);

    MarathonDetailResponse getMarathonById(int id);

    void deleteMarathon(int id);

    MarathonSearchResponse searchMarathonList(MarathonSearchRequest marathonSearchRequest);

    void updateMarathon(MarathonDetailRequest marathonDetailRequest,
                        MultipartFile thumbnailFile,
                        MultipartFile infoImageFile,
                        MultipartFile courseImageFile);
}
