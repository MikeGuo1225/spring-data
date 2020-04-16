package com.springdata.jpa.dto;

import java.io.IOException;
import java.io.InputStream;
import javax.validation.constraints.NotNull;
import lombok.Cleanup;
import lombok.Data;

import java.math.BigDecimal;
import lombok.SneakyThrows;
import lombok.val;

@Data
public class OrderDTO {
    @val
    @NotNull
    private String id;
    private BigDecimal price;
    private String type;

    @SneakyThrows
    private void test() {
        try {
            @Cleanup InputStream stream = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
        } catch (Exception e) {

        }
    }
}
