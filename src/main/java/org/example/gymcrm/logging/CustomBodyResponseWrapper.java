package org.example.gymcrm.logging;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomBodyResponseWrapper extends HttpServletResponseWrapper {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintWriter writer = new PrintWriter(outputStream);
  private boolean usingWriter;

  public CustomBodyResponseWrapper(HttpServletResponse response) {
    super(response);
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    usingWriter = true;
    return writer;
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (usingWriter) {
      throw new IllegalStateException("getWriter() has already been called");
    }
    return new ServletOutputStream() {
      @Override
      public void write(int b) throws IOException {
        outputStream.write(b);
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setWriteListener(WriteListener writeListener) {}
    };
  }

  public String getResponseBody() {
    writer.flush();
    return outputStream.toString();
  }

  @Override
  public void flushBuffer() throws IOException {
    writer.flush();
    outputStream.flush();
    super.flushBuffer();
  }
}
