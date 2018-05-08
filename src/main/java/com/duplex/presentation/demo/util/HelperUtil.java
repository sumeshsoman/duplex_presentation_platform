package com.duplex.presentation.demo.util;

import com.duplex.presentation.demo.dto.CSVDataDTO;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class HelperUtil {

  public static List<CSVDataDTO> generateCSVData(String jsonString) {
    CSVDataDTO[] csvDataDTOS = new Gson().fromJson(jsonString, CSVDataDTO[].class);
    return Arrays.asList(csvDataDTOS);
  }
}
