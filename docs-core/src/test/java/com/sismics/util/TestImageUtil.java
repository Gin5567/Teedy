package com.sismics.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of the image utilities.
 */
public class TestImageUtil {

    @Test
    public void computeGravatarTest_NormalizedEmail() {
        String result = ImageUtil.computeGravatar("MyEmailAddress@example.com ");
        Assert.assertEquals("0bc83cb571cd1c50ba6f3e8a78ef1346", result);
    }

    @Test
    public void computeGravatarTest_UpperCaseEmail() {
        String result = ImageUtil.computeGravatar("MYEMAILADDRESS@EXAMPLE.COM");
        Assert.assertEquals("0bc83cb571cd1c50ba6f3e8a78ef1346", result);
    }

    @Test
    public void computeGravatarTest_TrimSpaces() {
        String result = ImageUtil.computeGravatar("  myemailaddress@example.com  ");
        Assert.assertEquals("0bc83cb571cd1c50ba6f3e8a78ef1346", result);
    }

    @Test
    public void computeGravatarTest_EmptyEmail() {
        String result = ImageUtil.computeGravatar("");
        Assert.assertEquals("d41d8cd98f00b204e9800998ecf8427e", result); // md5 of ""
    }

    @Test
    public void computeGravatarTest_NullEmail() {
        String result = ImageUtil.computeGravatar(null);
        Assert.assertNull(result); // 你的实现返回 null，不抛异常
    }

    @Test
    public void computeGravatarTest_SpecialCharacters() {
        String result = ImageUtil.computeGravatar("user+tag@example.co.uk");
        Assert.assertEquals("580e13b0da3b68a7022ed2cbd4a698cc", result);
    }
}
