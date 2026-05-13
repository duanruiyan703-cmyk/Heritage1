package com.ruangong.heritage.dto.command;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeritageItemListQueryDTO {
    //当前页
    private Integer currentPage = 1;

    //每页大小
    private Integer size = 10;

    //标题关键词")
    private String title;

    //类别")
    private String category;

    //地区")
    private String region;

    //状态 0草稿 1待审 2已发布 3下架"
    private Integer status;

    //创建人ID")
    private String creatorId;

    //开始时间(格式: yyyy-MM-dd)"
    private String startDate;

    //结束时间(格式: yyyy-MM-dd)"
    private String endDate;

    //排序字段", example = "create_time"
    private String orderBy = "create_time";

    //排序方向", example = "desc"
    private String orderDirection = "desc";
}
