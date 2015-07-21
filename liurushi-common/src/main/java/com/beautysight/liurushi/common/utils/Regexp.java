/*
 * Copyright (C) 2014, BeautySight Inc. All rights reserved.
 */

package com.beautysight.liurushi.common.utils;

import java.util.regex.Pattern;

/**
 * Regular expression utils
 *
 * @author chenlong
 * @since 1.0
 */
public class Regexp {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    private static final Pattern MOBILE_WITH_CALLING_CODE_PATTERN = Pattern.compile("^\\+\\d+(-\\d+)?-1[3458]\\d{9}$");
    private static final Pattern CHINA_MOBILE_PATTERN = Pattern.compile("^1[3458]\\d{9}$");

    public static boolean isEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号码，支持国际格式。合法示例：+1-345-15824464622、+86-15824464622
     *
     * @param mobile 移动、联通、电信运营商的号码段。
     *               <p/>
     *               移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     *               <p/>
     *               联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     *               <p/>
     *               电信的号段：133、153、180（未启用）、189
     * @return true 格式合法；false 格式不合法
     */
    public static boolean isMobileWithCallingCode(String mobile) {
        return MOBILE_WITH_CALLING_CODE_PATTERN.matcher(mobile).matches();
    }

    public static boolean isChinaMobileWithoutCallingCode(String mobile) {
        return CHINA_MOBILE_PATTERN.matcher(mobile).matches();
    }

}
