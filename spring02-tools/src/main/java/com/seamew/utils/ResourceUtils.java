package com.seamew.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ResourceUtils
{
    public static String getContent(Resource resource)
    {
        StringBuilder sb = new StringBuilder(64);
        byte[] buffer = new byte[1024];
        int readLen;
        try (InputStream in = resource.getInputStream())
        {
            while ((readLen = in.read(buffer)) != -1)
            {
                sb.append(new String(buffer, 0, readLen));
            }
        }
        catch (IOException e)
        {
            log.error("Error occurred while reading inputStream\nCause:", e);
        }
        return sb.toString();
    }
}
