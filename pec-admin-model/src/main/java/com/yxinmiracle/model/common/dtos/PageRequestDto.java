package com.yxinmiracle.model.common.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
public class PageRequestDto {

    protected Integer size;
    protected Integer page;

    protected Integer start;
    protected Integer end;


    public void checkParam() {
        if (this.page == null || this.page < 0) {
            setPage(1);
        }
        if (this.size == null || this.size < 0 || this.size > 100) {
            setSize(10);
        }
    }


    public Integer getStart() {
        return this.page == 1 ? 0 : (this.page - 1) * this.size + 1;
    }

    public Integer getEnd() {
        return this.page * this.size;
    }
}
