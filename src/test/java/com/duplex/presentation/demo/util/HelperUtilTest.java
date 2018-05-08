package com.duplex.presentation.demo.util;

import com.duplex.presentation.demo.dto.CSVDataDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class HelperUtilTest {

  @Test
  public void generateCSVData() {
    String inputJsonData =
        "[{\"name\":\"Joe\",\"age\":12,\"height\":\"5.1\"},{\"name\":\"Alice\",\"age\":32,\"height\":\"5.7\"},{\"name\":\"Madden\",\"age\":55,\"height\":\"5.9\"},{\"name\":\"Alfred\",\"age\":21,\"height\":\"6.2\"}]";

    List<CSVDataDTO> csvDataDTOList = HelperUtil.generateCSVData(inputJsonData);
    Assert.assertEquals(4, csvDataDTOList.size());
  }
}
