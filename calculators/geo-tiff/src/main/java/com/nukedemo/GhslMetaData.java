package com.nukedemo;

public class GhslMetaData {
    private int areaWidth;
    private int  areaHeight;
    //[lon,lat]
    private double[] topRightCorner;
    private double[] bottomLeftCorner;
    private double[] topLeftCorner;
    private double[] bottomRightCorner;
    private double pixelHeightDegrees;
    private double pixelWidthDegrees;
    private int totalPixelCount;
    private int totalPixelValue;

    private GhslMetaData() {}

    public static Builder builder() {
        return new Builder();
    }

    public int getAreaWidth() {
        return areaWidth;
    }

    public int getAreaHeight() {
        return areaHeight;
    }

    public double[] getTopRightCorner() {
        return topRightCorner;
    }

    public double[] getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public double[] getTopLeftCorner() {
        return topLeftCorner;
    }

    public double[] getBottomRightCorner() {
        return bottomRightCorner;
    }

    public double getPixelHeightDegrees() {
        return pixelHeightDegrees;
    }

    public double getPixelWidthDegrees() {
        return pixelWidthDegrees;
    }

    public int getTotalPixelCount() {
        return totalPixelCount;
    }

    public int getTotalPixelValue() {
        return totalPixelValue;
    }

    public static final class Builder {
        private int areaWidth;
        private int areaHeight;
        private double[] topRightCorner;
        private double[] bottomLeftCorner;
        private double[] topLeftCorner;
        private double[] bottomRightCorner;
        private double pixelHeightDegrees;
        private double pixelWidthDegrees;
        private int totalPixelCount;
        private int totalPixelValue;

        private Builder() {
        }

        public Builder withAreaWidth(int areaWidth) {
            this.areaWidth = areaWidth;
            return this;
        }

        public Builder withAreaHeight(int areaHeight) {
            this.areaHeight = areaHeight;
            return this;
        }

        public Builder withTopRightCorner(double[] topRightCorner) {
            this.topRightCorner = topRightCorner;
            return this;
        }

        public Builder withBottomLeftCorner(double[] bottomLeftCorner) {
            this.bottomLeftCorner = bottomLeftCorner;
            return this;
        }

        public Builder withTopLeftCorner(double[] topLeftCorner) {
            this.topLeftCorner = topLeftCorner;
            return this;
        }

        public Builder withBottomRightCorner(double[] bottomRightCorner) {
            this.bottomRightCorner = bottomRightCorner;
            return this;
        }

        public Builder withPixelHeightDegrees(double pixelHeightDegrees) {
            this.pixelHeightDegrees = pixelHeightDegrees;
            return this;
        }

        public Builder withPixelWidthDegrees(double pixelWidthDegrees) {
            this.pixelWidthDegrees = pixelWidthDegrees;
            return this;
        }

        public Builder withTotalPixelCount(int totalPixelCount) {
            this.totalPixelCount = totalPixelCount;
            return this;
        }

        public Builder withTotalPixelValue(int totalPixelValue) {
            this.totalPixelValue = totalPixelValue;
            return this;
        }

        public GhslMetaData build() {
            GhslMetaData ghslMetaData = new GhslMetaData();
            ghslMetaData.areaWidth = this.areaWidth;
            ghslMetaData.totalPixelCount = this.totalPixelCount;
            ghslMetaData.areaHeight = this.areaHeight;
            ghslMetaData.pixelHeightDegrees = this.pixelHeightDegrees;
            ghslMetaData.bottomRightCorner = this.bottomRightCorner;
            ghslMetaData.bottomLeftCorner = this.bottomLeftCorner;
            ghslMetaData.topLeftCorner = this.topLeftCorner;
            ghslMetaData.topRightCorner = this.topRightCorner;
            ghslMetaData.totalPixelValue = this.totalPixelValue;
            ghslMetaData.pixelWidthDegrees = this.pixelWidthDegrees;
            return ghslMetaData;
        }
    }
}
