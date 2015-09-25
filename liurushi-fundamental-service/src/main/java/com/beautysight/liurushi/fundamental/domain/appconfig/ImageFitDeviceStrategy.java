/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.fundamental.domain.appconfig;

import com.beautysight.liurushi.common.ex.ApplicationException;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 该类表示图片在不同设备上的适配策略。
 *
 * @author chenlong
 * @since 1.0
 */
public class ImageFitDeviceStrategy {

    private Map<String, List<Integer>> apiToSpecsMap;

    public void setApiToSpecsMap(Map<String, List<Integer>> apiToSpecsMap) {
        this.apiToSpecsMap = apiToSpecsMap;
    }

    public Integer bestImageWidthFor(Integer deviceResolutionWidth, String restApiUri) {
        if (CollectionUtils.isEmpty(this.apiToSpecsMap)) {
            throw new ApplicationException("App config item %s: no config val",
                    AppConfig.ItemName.image_fit_device_strategy);
        }

        List<Integer> specs = apiToSpecsMap.get(restApiUri);

        if (CollectionUtils.isEmpty(specs)) {
            throw new ApplicationException("App config item %s: no corresponding images specs for %s",
                    AppConfig.ItemName.image_fit_device_strategy, restApiUri);
        }

        return calculateBestImgWidthBy(deviceResolutionWidth, specs);
    }

    public static Integer calculateBestImgWidthBy(Integer deviceResolutionWidth, List<Integer> specs) {
        if (CollectionUtils.isEmpty(specs)) {
            throw new ApplicationException("Image thumbnail spec is blank");
        }

        Collections.sort(specs);

        Integer smaller = specs.get(0), bigger = specs.get(specs.size() - 1);
        if (smaller < deviceResolutionWidth && deviceResolutionWidth < bigger) {
            for (Integer spec : specs) {
                if (spec >= deviceResolutionWidth) {
                    bigger = spec;
                    break;
                } else {
                    smaller = spec;
                }
            }
        }

        int diffWithSmaller = Math.abs(deviceResolutionWidth - smaller);
        int diffWithBigger = Math.abs(deviceResolutionWidth - bigger);

        if (diffWithSmaller <= diffWithBigger) {
            return smaller;
        }

        return bigger;
    }

}
