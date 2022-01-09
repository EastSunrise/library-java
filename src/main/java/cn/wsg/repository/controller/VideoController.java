package cn.wsg.repository.controller;

import cn.wsg.commons.internet.com.douban.DoubanVideo;
import cn.wsg.repository.common.dto.QueryVideoDTO;
import cn.wsg.repository.common.error.DataIntegrityException;
import cn.wsg.repository.service.VideoService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Video-related apis.
 *
 * @author Kingen
 */
@Controller
@RequestMapping("/api/video")
public class VideoController extends BaseController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/count")
    public ResponseEntity<Integer> countVideo(@RequestBody QueryVideoDTO queryVideoDTO) {
        return ResponseEntity.ok(videoService.countVideoBy(queryVideoDTO));
    }

    @PostMapping("/import/subject")
    public ResponseEntity<String> importSubject(Long id, @RequestBody DoubanVideo video) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Lacking of id");
        }
        if (video == null) {
            return ResponseEntity.badRequest().body("Lacking of data");
        }
        try {
            videoService.importVideoFromDouban(id, video);
        } catch (DataIntegrityException e) {
            return ResponseEntity.status(HttpStatus.SC_UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
        return SUCCESS;
    }
}
