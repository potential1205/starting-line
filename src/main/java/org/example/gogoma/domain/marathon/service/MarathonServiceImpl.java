package org.example.gogoma.domain.marathon.service;

import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gogoma.common.util.GoogleDocumentAiUtil;
import org.example.gogoma.common.util.GoogleVisionApiUtil;
import org.example.gogoma.common.util.S3ImageUtil;
import org.example.gogoma.controller.request.CreateMarathonRequest;
import org.example.gogoma.controller.request.MarathonDetailRequest;
import org.example.gogoma.controller.response.MarathonDetailResponse;
import org.example.gogoma.domain.marathon.dto.CustomMultipartFile;
import org.example.gogoma.domain.marathon.dto.MarathonDataDto;
import org.example.gogoma.domain.marathon.entity.Marathon;
import org.example.gogoma.domain.marathon.entity.MarathonType;
import org.example.gogoma.domain.marathon.enums.MarathonStatus;
import org.example.gogoma.domain.marathon.repository.MarathonRepository;
import org.example.gogoma.domain.marathon.repository.MarathonTypeRepository;
import org.example.gogoma.exception.ExceptionCode;
import org.example.gogoma.exception.type.BusinessException;
import org.example.gogoma.exception.type.DbException;
import org.example.gogoma.external.openai.ChatGptClient;
import org.example.gogoma.external.openai.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarathonServiceImpl implements MarathonService {

    private final S3ImageUtil s3ImageUtil;
    private final ChatGptClient chatGptClient;
    private final GoogleDocumentAiUtil googleDocumentAiUtil;
    private final GoogleVisionApiUtil googleVisionApiUtil;
    private final MarathonRepository marathonRepository;
    private final MarathonTypeRepository marathonTypeRepository;
    private static final Gson gson = new Gson();

    @Override
    @Transactional
    public void createMarathon(CreateMarathonRequest createMarathonRequest,
                               MultipartFile thumbnailFile,
                               MultipartFile infoImageFile,
                               MultipartFile courseImageFile) {

        try {
            log.info("Processing marathon creation...");

            MultipartFile multipartFile;
            String thumbnailImageUrl = "";
            String courseImageUrl = "";
            String infoImageUrl = "";
            String information = "";
            String prompt = Prompt.DEFAULT.getPrompt();

            // 썸네일 이미지 저장
            if (createMarathonRequest.getThumbnailUrl() != null && !createMarathonRequest.getThumbnailUrl().isBlank()) {
                multipartFile = convertUrlToMultipartFile(createMarathonRequest.getThumbnailUrl());
                thumbnailImageUrl = s3ImageUtil.uploadImage(multipartFile, "marathon/thumbnail");
            } else if (thumbnailFile.getSize() > 0) {
                thumbnailImageUrl = s3ImageUtil.uploadImage(thumbnailFile, "marathon/thumbnail");
            }

            // 코스 이미지 저장
            if (createMarathonRequest.getCourseImageUrl() != null && !createMarathonRequest.getCourseImageUrl().isBlank()) {
                multipartFile = convertUrlToMultipartFile(createMarathonRequest.getCourseImageUrl());
                courseImageUrl = s3ImageUtil.uploadImage(multipartFile, "marathon/course");
            } else if (courseImageFile.getSize() > 0) {
                courseImageUrl = s3ImageUtil.uploadImage(courseImageFile, "marathon/course");
            }

            // 대회 요강 이미지 저장
            if (createMarathonRequest.getInfoImageUrl() != null && !createMarathonRequest.getInfoImageUrl().isBlank()) {
                multipartFile = convertUrlToMultipartFile(createMarathonRequest.getInfoImageUrl());
                infoImageUrl = s3ImageUtil.uploadImage(multipartFile, "marathon/info");
                prompt += getPrompt(createMarathonRequest.getInfoImageUrl());
            } else if (infoImageFile.getSize() > 0) {
                infoImageUrl = s3ImageUtil.uploadImage(infoImageFile, "marathon/info");
                prompt += getPrompt(infoImageFile);
            }

            prompt += createMarathonRequest.getTextInfo();

            information = chatGptClient.chat(prompt);

            MarathonDataDto data = gson.fromJson(information, MarathonDataDto.class);

            LocalDateTime registrationStartDateTime = parseTime(data.getMarathonInfo().getRegistrationStartDateTime());
            LocalDateTime registrationEndDateTime = parseTime(data.getMarathonInfo().getRegistrationEndDateTime());
            LocalDateTime raceStartTime = parseTime(data.getMarathonInfo().getRaceStartTime());

            Marathon marathon = Marathon.builder()
                    .title(data.getMarathonInfo().getTitle())
                    .registrationStartDateTime(registrationStartDateTime)
                    .registrationEndDateTime(registrationEndDateTime)
                    .raceStartTime(raceStartTime)
                    .accountBank(data.getMarathonInfo().getAccountBank())
                    .accountNumber(data.getMarathonInfo().getAccountNumber())
                    .accountName(data.getMarathonInfo().getAccountName())
                    .location(data.getMarathonInfo().getLocation())
                    .hostList(data.getMarathonInfo().getHostList())
                    .organizerList(data.getMarathonInfo().getOrganizerList())
                    .sponsorList(data.getMarathonInfo().getSponsorList())
                    .qualifications(data.getMarathonInfo().getQualifications())
                    .viewCount(0)
                    .marathonStatus(MarathonStatus.OPEN)
                    .thumbnailImage(thumbnailImageUrl)
                    .courseImage(courseImageUrl)
                    .infoImage(infoImageUrl)
                    .build();

            marathonRepository.save(marathon);

            List<MarathonType> marathonTypeList = data.getMarathonInfo().getMarathonTypeDtoList().stream()
                    .map(dto -> MarathonType.of(dto, marathon.getId()))
                    .toList();

            marathonTypeRepository.saveAll(marathonTypeList);

        } catch (IOException e) {
            throw new BusinessException(ExceptionCode.BUSINESS_ERROR);
        }
    }

    private MultipartFile convertUrlToMultipartFile(String imageUrl) throws IOException {
        byte[] imageBytes = downloadImageBytes(imageUrl);
        return new CustomMultipartFile(imageBytes, imageUrl);
    }

    private byte[] downloadImageBytes(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        try (InputStream inputStream = url.openStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            BufferedImage urlImage = ImageIO.read(inputStream);
            ImageIO.write(urlImage, "jpg", bos);
            return bos.toByteArray();
        }
    }

    private String getPrompt(String imageUrl){
        return googleVisionApiUtil.extractTextFromImageUrl(imageUrl) + "\n" +
                googleDocumentAiUtil.extractTextFromImageUrl(imageUrl);
    }

    private String getPrompt(MultipartFile imageFile) {
        return googleVisionApiUtil.extractTextFromImage(imageFile) + "\n" +
                googleDocumentAiUtil.extractTextFromImage(imageFile);
    }

    /**
     * 문자열을 LocalDateTime으로 변환
     */
    private LocalDateTime parseTime(String dateTime) {
        return Optional.ofNullable(dateTime)
                .filter(date -> !date.isBlank())
                .map(LocalDateTime::parse)
                .orElse(null);
    }

    @Override
    public MarathonDetailResponse getMarathonById(int id) {
        Marathon marathon = marathonRepository.findById(id)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        List<MarathonType> marathonTypeList = marathonTypeRepository.findAllByMarathonId(id);

        return MarathonDetailResponse.of(marathon, marathonTypeList);
    }

    @Override
    @Transactional
    public void updateMarathon(MarathonDetailRequest marathonDetailRequest) {
        Marathon marathon = marathonRepository.findById(marathonDetailRequest.getMarathon().getId())
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        marathon.update(
                marathonDetailRequest.getMarathon().getTitle(),
                marathonDetailRequest.getMarathon().getRegistrationStartDateTime(),
                marathonDetailRequest.getMarathon().getRegistrationEndDateTime(),
                marathonDetailRequest.getMarathon().getRaceStartTime(),
                marathonDetailRequest.getMarathon().getAccountBank(),
                marathonDetailRequest.getMarathon().getAccountNumber(),
                marathonDetailRequest.getMarathon().getAccountName(),
                marathonDetailRequest.getMarathon().getLocation(),
                marathonDetailRequest.getMarathon().getQualifications(),
                marathonDetailRequest.getMarathon().getMarathonStatus()
        );

        marathonTypeRepository.deleteAllByMarathonId(marathon.getId());

        List<MarathonType> marathonTypeList = marathonDetailRequest.getMarathonTypeList().stream()
                .map(type -> MarathonType.builder()
                        .marathonId(marathon.getId())
                        .courseType(type.getCourseType())
                        .price(type.getPrice())
                        .etc(type.getEtc())
                        .build())
                .toList();


        marathonTypeRepository.saveAll(marathonTypeList);
    }

    @Override
    @Transactional
    public void deleteMarathon(int id) {
        Marathon marathon = marathonRepository.findById(id)
                .orElseThrow(() -> new DbException(ExceptionCode.MARATHON_NOT_FOUND));

        if (marathon.getThumbnailImage() != null && !marathon.getThumbnailImage().isBlank()) {
            s3ImageUtil.deleteImageByUrl(marathon.getThumbnailImage());
        }

        if (marathon.getInfoImage() != null && !marathon.getInfoImage().isBlank()) {
            s3ImageUtil.deleteImageByUrl(marathon.getInfoImage());
        }

        if (marathon.getCourseImage() != null && !marathon.getCourseImage().isBlank()) {
            s3ImageUtil.deleteImageByUrl(marathon.getCourseImage());
        }

        marathonRepository.deleteById(marathon.getId());

        marathonTypeRepository.deleteAllByMarathonId(marathon.getId());
    }
}
