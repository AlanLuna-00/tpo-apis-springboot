package uade.apis.backend.shared.responses;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> items;
    private Meta meta;

    @Data
    @AllArgsConstructor
    public static class Meta {
        private int page;
        private int size;
        private long totalItems;
        private int totalPages;
    }
}
