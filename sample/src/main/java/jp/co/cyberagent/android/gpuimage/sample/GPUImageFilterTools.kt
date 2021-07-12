/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.sample

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.opengl.Matrix
import jp.co.cyberagent.android.gpuimage.filter.*
import jp.co.cyberagent.android.gpuimage.testfilter.*
import java.util.*

object GPUImageFilterTools {
    fun showDialog(
            context: Context,
            listener: (filterName: String, filter: GPUImageFilter) -> Unit
    ) {
        val filters = FilterList().apply {
            addFilter("剪映横线效果（横线毛刺）", FilterType.HORIZONALLIE)
            addFilter("荧光线描", FilterType.EDGESOBEL)
            addFilter("水波纹", FilterType.RIPPLE)
            addFilter("浮雕效果", FilterType.EMBOSS2)
            addFilter("Blur(模糊效果)", FilterType.BLUR)
            addFilter("HotImg(热成像/彩色负片)", FilterType.HOTIMG)
            addFilter("ZoomFarBlur(运镜转场拉远模糊)", FilterType.ZOOMFAR)
            addFilter("FlashWhite(闪白)", FilterType.FLASHWHITE)
            addFilter("RoundRect(圆角矩形)", FilterType.ROUNDRECT)
            addFilter("ChromaticScale(色差放大capcut)", FilterType.CHROMATICSCALE)
            addFilter("Chromatic(波纹色差capcut)", FilterType.CHROMATIC)
            addFilter("Cube(立方体capcut)", FilterType.CUBE)
            addFilter("DoorWay(开幕capcut)", FilterType.DOORWAY)
            addFilter("PinWheel(风车capcut)", FilterType.PINWHEEL)
            addFilter("CircleScan(圆形扫描capcut)", FilterType.CIRCLESCAN)
            addFilter("InvertedPageCurl(翻页capcut)", FilterType.INVERTEDPAGECURL)
            addFilter("RotationalBlur(旋转模糊capcut)", FilterType.ROTATIONALBLUR)
            addFilter("Fake3d(tiktok)", FilterType.FAKE3D)
            addFilter("SourlScale(tiktok)", FilterType.SOULSCALE)
            addFilter("Contrast(对比度)", FilterType.CONTRAST)
            addFilter("Invert(反色)", FilterType.INVERT)
            addFilter("Pixelation(像素化)", FilterType.PIXELATION)
            addFilter("Hue(色度)", FilterType.HUE)
            addFilter("Gamma(伽马线)", FilterType.GAMMA)
            addFilter("Brightness(亮度)", FilterType.BRIGHTNESS)
            addFilter("Sepia(褐色怀旧)", FilterType.SEPIA)
            addFilter("Grayscale(灰度)", FilterType.GRAYSCALE)
            addFilter("Sharpness(锐化)", FilterType.SHARPEN)
            addFilter("Sobel Edge Detection(Sobel边缘检测算法(白边，黑内容，有点漫画的反色效果))", FilterType.SOBEL_EDGE_DETECTION)
            addFilter("Threshold Edge Detection(阈值边缘检测)", FilterType.THRESHOLD_EDGE_DETECTION)
            addFilter("3x3 Convolution(3x3卷积，高亮大色块变黑，加亮边缘、线条等)", FilterType.THREE_X_THREE_CONVOLUTION)
            addFilter("Emboss(浮雕效果，带有点3d的感觉)", FilterType.EMBOSS)
            addFilter("Posterize(色调分离，形成噪点效果)", FilterType.POSTERIZE)
            addFilter("Grouped filters", FilterType.FILTER_GROUP)
            addFilter("Saturation(饱和度)", FilterType.SATURATION)
            addFilter("Exposure(曝光)", FilterType.EXPOSURE)
            addFilter("Highlight Shadow(提亮阴影)", FilterType.HIGHLIGHT_SHADOW)
            addFilter("Monochrome(单色)", FilterType.MONOCHROME)
            addFilter("Opacity(不透明度)", FilterType.OPACITY)
            addFilter("RGB(调整 RED)", FilterType.RGB)
            addFilter("White Balance(白平横)", FilterType.WHITE_BALANCE)
            addFilter("Vignette(晕影，形成黑色圆形边缘，突出中间图像的效果)", FilterType.VIGNETTE)
            addFilter("ToneCurve(色调曲线)", FilterType.TONE_CURVE)

            addFilter("Luminance", FilterType.LUMINANCE)
            addFilter("Luminance Threshold(亮度阈)", FilterType.LUMINANCE_THRESHSOLD)

            //混合模式
            addFilter("Blend (Difference 差异混合,通常用于创建更多变动的颜色)", FilterType.BLEND_DIFFERENCE)
            addFilter("Blend (Source Over 源混合)", FilterType.BLEND_SOURCE_OVER)
            addFilter("Blend (Color Burn 色彩加深混合)", FilterType.BLEND_COLOR_BURN)
            addFilter("Blend (Color Dodge 色彩减淡混合)", FilterType.BLEND_COLOR_DODGE)
            addFilter("Blend (Darken 加深混合,通常用于重叠类型)", FilterType.BLEND_DARKEN)
            addFilter("Blend (Dissolve 溶解)", FilterType.BLEND_DISSOLVE)
            addFilter("Blend (Exclusion 排除混合)", FilterType.BLEND_EXCLUSION)
            addFilter("Blend (Hard Light 强光混合,通常用于创建阴影效果)", FilterType.BLEND_HARD_LIGHT)
            addFilter("Blend (Lighten 减淡混合,通常用于重叠类型)", FilterType.BLEND_LIGHTEN)
            addFilter("Blend (Add 通常用于创建两个图像之间的动画变亮模糊效果)", FilterType.BLEND_ADD)
            addFilter("Blend (Divide 通常用于创建两个图像之间的动画变暗模糊效果)", FilterType.BLEND_DIVIDE)
            addFilter("Blend (Multiply 通常用于创建阴影和深度效果)", FilterType.BLEND_MULTIPLY)
            addFilter("Blend (Overlay 叠加,通常用于创建阴影效果)", FilterType.BLEND_OVERLAY)
            addFilter("Blend (Screen 屏幕包裹,通常用于创建亮点和镜头眩光)", FilterType.BLEND_SCREEN)
            addFilter("Blend (Alpha 透明混合,通常用于在背景上应用前景的透明度)", FilterType.BLEND_ALPHA)
            addFilter("Blend (Color)", FilterType.BLEND_COLOR)
            addFilter("Blend (Hue)", FilterType.BLEND_HUE)
            addFilter("Blend (Saturation)", FilterType.BLEND_SATURATION)
            addFilter("Blend (Luminosity)", FilterType.BLEND_LUMINOSITY)
            addFilter("Blend (Linear Burn)", FilterType.BLEND_LINEAR_BURN)
            addFilter("Blend (Soft Light 柔光混合)", FilterType.BLEND_SOFT_LIGHT)
            addFilter("Blend (Subtract 差值混合,通常用于创建两个图像之间的动画变暗模糊效果)", FilterType.BLEND_SUBTRACT)
            addFilter("Blend (Chroma Key 色度键混合)", FilterType.BLEND_CHROMA_KEY)
            addFilter("Blend (Normal 正常)", FilterType.BLEND_NORMAL)

            addFilter("Lookup (Amatorka 色彩调整)", FilterType.LOOKUP_AMATORKA)
            addFilter("Gaussian Blur(高斯模糊)", FilterType.GAUSSIAN_BLUR)
            addFilter("Crosshatch(交叉线阴影，形成黑白网状画面)", FilterType.CROSSHATCH)

            addFilter("Box Blur(盒状模糊)", FilterType.BOX_BLUR)
            addFilter("CGA Color Space(CGA色彩滤镜，形成黑、浅蓝、紫色块的画面)", FilterType.CGA_COLORSPACE)
            addFilter("Dilation(Dilation)", FilterType.DILATION)
            addFilter("Kuwahara(桑原(Kuwahara)滤波,水粉画的模糊效果；处理时间比较长，慎用)", FilterType.KUWAHARA)
            addFilter("RGB Dilation(RGB扩展边缘模糊，有色彩)", FilterType.RGB_DILATION)
            addFilter("Sketch(素描)", FilterType.SKETCH)
            addFilter("Toon(卡通效果:黑色粗线描边)", FilterType.TOON)
            addFilter("Smooth Toon(相比上面的效果更细腻，上面是粗旷的画风)", FilterType.SMOOTH_TOON)
            addFilter("Halftone(点染,图像黑白化，由黑点构成原图的大致图形)", FilterType.HALFTONE)

            addFilter("Bulge Distortion(凸起失真，鱼眼效果)", FilterType.BULGE_DISTORTION)
            addFilter("Glass Sphere(水晶球效果)", FilterType.GLASS_SPHERE)
            addFilter("Haze(朦胧加暗)", FilterType.HAZE)
            addFilter("Laplacian(拉普拉斯矩阵变换)", FilterType.LAPLACIAN)
            addFilter("Non Maximum Suppression(非最大抑制，只显示亮度最高的像素，其他为黑)", FilterType.NON_MAXIMUM_SUPPRESSION)
            addFilter("Sphere Refraction(球形折射，图形倒立)", FilterType.SPHERE_REFRACTION)
            addFilter("Swirl(漩涡，中间形成卷曲的画面)", FilterType.SWIRL)
            addFilter("Weak Pixel Inclusion", FilterType.WEAK_PIXEL_INCLUSION)
            addFilter("False Color(色彩替换:替换亮部和暗部色彩)", FilterType.FALSE_COLOR)

            addFilter("Color Balance", FilterType.COLOR_BALANCE)

            addFilter("Levels Min (Mid Adjust)", FilterType.LEVELS_FILTER_MIN)

            addFilter("Bilateral Blur(双边模糊，保留锐利边缘的同时模糊相似的颜色值)", FilterType.BILATERAL_BLUR)

            addFilter("Zoom Blur(定向运动模糊)", FilterType.ZOOM_BLUR)

            addFilter("Transform (2-D 2-D 空间图像变形)", FilterType.TRANSFORM2D)

            addFilter("Solarize(日晒)", FilterType.SOLARIZE)

            addFilter("Vibrance(调整图像的振动)", FilterType.VIBRANCE)
        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose a filter")
        builder.setItems(filters.names.toTypedArray()) { _, item ->
            listener(filters.names[item], createFilterForType(context, filters.filters[item]))

        }
        builder.create().show()
    }

    private fun createFilterForType(context: Context, type: FilterType): GPUImageFilter {
        return when (type) {
            FilterType.HORIZONALLIE -> HorizonalLineFilter(context)
            FilterType.EDGESOBEL -> EdgeSobelFilter(context)
            FilterType.RIPPLE -> RippleFilter(context)
            FilterType.EMBOSS2 -> EmbossFilter2(context)
            FilterType.BLUR -> BlurFilter(context)
            FilterType.HOTIMG -> HotImgFilter(context).apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ccc_color)
            }
            FilterType.ZOOMFAR -> ZoomFarFilter(context)
            FilterType.FLASHWHITE -> FlashWhiteFilter(context)
            FilterType.ROUNDRECT -> RoundRectFilter(context)
            FilterType.CHROMATIC -> ChromaticFilter(context)
            FilterType.CHROMATICSCALE -> ChromaticScaleFilter(context)
            FilterType.CUBE -> CubeFilter(context)
            FilterType.DOORWAY -> DoorWaylFilter(context)
            FilterType.PINWHEEL -> PinWheellFilter(context)
            FilterType.CIRCLESCAN -> CircleScanFilter(context)
            FilterType.INVERTEDPAGECURL -> InvertedPageCurlFilter(context)
            FilterType.ROTATIONALBLUR -> RotationBlurFilter(context)
            FilterType.FAKE3D -> Fake3dFilter(context)
            FilterType.SOULSCALE -> SoulScaleFilter(context)
            FilterType.CONTRAST -> GPUImageContrastFilter(2.0f)
            FilterType.GAMMA -> GPUImageGammaFilter(2.0f)
            FilterType.INVERT -> GPUImageColorInvertFilter()
            FilterType.PIXELATION -> GPUImagePixelationFilter()
            FilterType.HUE -> GPUImageHueFilter(90.0f)
            FilterType.BRIGHTNESS -> GPUImageBrightnessFilter(1.5f)
            FilterType.GRAYSCALE -> GPUImageGrayscaleFilter()
            FilterType.SEPIA -> GPUImageSepiaToneFilter()
            FilterType.SHARPEN -> GPUImageSharpenFilter()
            FilterType.SOBEL_EDGE_DETECTION -> GPUImageSobelEdgeDetectionFilter()
            FilterType.THRESHOLD_EDGE_DETECTION -> GPUImageThresholdEdgeDetectionFilter()
            FilterType.THREE_X_THREE_CONVOLUTION -> GPUImage3x3ConvolutionFilter()
            FilterType.EMBOSS -> GPUImageEmbossFilter()
            FilterType.POSTERIZE -> GPUImagePosterizeFilter()
            FilterType.FILTER_GROUP -> GPUImageFilterGroup(
                    listOf(
                            GPUImageContrastFilter(),
                            GPUImageDirectionalSobelEdgeDetectionFilter(),
                            GPUImageGrayscaleFilter()
                    )
            )
            FilterType.SATURATION -> GPUImageSaturationFilter(1.0f)
            FilterType.EXPOSURE -> GPUImageExposureFilter(0.0f)
            FilterType.HIGHLIGHT_SHADOW -> GPUImageHighlightShadowFilter(
                    0.0f,
                    1.0f
            )
            FilterType.MONOCHROME -> GPUImageMonochromeFilter(
                    1.0f, floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)
            )
            FilterType.OPACITY -> GPUImageOpacityFilter(1.0f)
            FilterType.RGB -> GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            FilterType.WHITE_BALANCE -> GPUImageWhiteBalanceFilter(
                    5000.0f,
                    0.0f
            )
            FilterType.VIGNETTE -> GPUImageVignetteFilter(
                    PointF(0.5f, 0.5f),
                    floatArrayOf(0.0f, 0.0f, 0.0f),
                    0.3f,
                    0.75f
            )
            FilterType.TONE_CURVE -> GPUImageToneCurveFilter().apply {
                setFromCurveFileInputStream(context.resources.openRawResource(R.raw.tone_cuver_sample))
            }
            FilterType.LUMINANCE -> GPUImageLuminanceFilter()
            FilterType.LUMINANCE_THRESHSOLD -> GPUImageLuminanceThresholdFilter(0.5f)
            FilterType.BLEND_DIFFERENCE -> createBlendFilter(
                    context,
                    GPUImageDifferenceBlendFilter::class.java
            )
            FilterType.BLEND_SOURCE_OVER -> createBlendFilter(
                    context,
                    GPUImageSourceOverBlendFilter::class.java
            )
            FilterType.BLEND_COLOR_BURN -> createBlendFilter(
                    context,
                    GPUImageColorBurnBlendFilter::class.java
            )
            FilterType.BLEND_COLOR_DODGE -> createBlendFilter(
                    context,
                    GPUImageColorDodgeBlendFilter::class.java
            )
            FilterType.BLEND_DARKEN -> createBlendFilter(
                    context,
                    GPUImageDarkenBlendFilter::class.java
            )
            FilterType.BLEND_DISSOLVE -> createBlendFilter(
                    context,
                    GPUImageDissolveBlendFilter::class.java
            )
            FilterType.BLEND_EXCLUSION -> createBlendFilter(
                    context,
                    GPUImageExclusionBlendFilter::class.java
            )

            FilterType.BLEND_HARD_LIGHT -> createBlendFilter(
                    context,
                    GPUImageHardLightBlendFilter::class.java
            )
            FilterType.BLEND_LIGHTEN -> createBlendFilter(
                    context,
                    GPUImageLightenBlendFilter::class.java
            )
            FilterType.BLEND_ADD -> createBlendFilter(
                    context,
                    GPUImageAddBlendFilter::class.java
            )
            FilterType.BLEND_DIVIDE -> createBlendFilter(
                    context,
                    GPUImageDivideBlendFilter::class.java
            )
            FilterType.BLEND_MULTIPLY -> createBlendFilter(
                    context,
                    GPUImageMultiplyBlendFilter::class.java
            )
            FilterType.BLEND_OVERLAY -> createBlendFilter(
                    context,
                    GPUImageOverlayBlendFilter::class.java
            )
            FilterType.BLEND_SCREEN -> createBlendFilter(
                    context,
                    GPUImageScreenBlendFilter::class.java
            )
            FilterType.BLEND_ALPHA -> createBlendFilter(
                    context,
                    GPUImageAlphaBlendFilter::class.java
            )
            FilterType.BLEND_COLOR -> createBlendFilter(
                    context,
                    GPUImageColorBlendFilter::class.java
            )
            FilterType.BLEND_HUE -> createBlendFilter(
                    context,
                    GPUImageHueBlendFilter::class.java
            )
            FilterType.BLEND_SATURATION -> createBlendFilter(
                    context,
                    GPUImageSaturationBlendFilter::class.java
            )
            FilterType.BLEND_LUMINOSITY -> createBlendFilter(
                    context,
                    GPUImageLuminosityBlendFilter::class.java
            )
            FilterType.BLEND_LINEAR_BURN -> createBlendFilter(
                    context,
                    GPUImageLinearBurnBlendFilter::class.java
            )
            FilterType.BLEND_SOFT_LIGHT -> createBlendFilter(
                    context,
                    GPUImageSoftLightBlendFilter::class.java
            )
            FilterType.BLEND_SUBTRACT -> createBlendFilter(
                    context,
                    GPUImageSubtractBlendFilter::class.java
            )
            FilterType.BLEND_CHROMA_KEY -> createBlendFilter(
                    context,
                    GPUImageChromaKeyBlendFilter::class.java
            )
            FilterType.BLEND_NORMAL -> createBlendFilter(
                    context,
                    GPUImageNormalBlendFilter::class.java
            )

            FilterType.LOOKUP_AMATORKA -> GPUImageLookupFilter().apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.beauty_filter_12_1_1)
            }
            FilterType.GAUSSIAN_BLUR -> GPUImageGaussianBlurFilter()
            FilterType.CROSSHATCH -> GPUImageCrosshatchFilter()
            FilterType.BOX_BLUR -> GPUImageBoxBlurFilter()
            FilterType.CGA_COLORSPACE -> GPUImageCGAColorspaceFilter()
            FilterType.DILATION -> GPUImageDilationFilter()
            FilterType.KUWAHARA -> GPUImageKuwaharaFilter()
            FilterType.RGB_DILATION -> GPUImageRGBDilationFilter()
            FilterType.SKETCH -> GPUImageSketchFilter()
            FilterType.TOON -> GPUImageToonFilter()
            FilterType.SMOOTH_TOON -> GPUImageSmoothToonFilter()
            FilterType.BULGE_DISTORTION -> GPUImageBulgeDistortionFilter()
            FilterType.GLASS_SPHERE -> GPUImageGlassSphereFilter()
            FilterType.HAZE -> GPUImageHazeFilter()
            FilterType.LAPLACIAN -> GPUImageLaplacianFilter()
            FilterType.NON_MAXIMUM_SUPPRESSION -> GPUImageNonMaximumSuppressionFilter()
            FilterType.SPHERE_REFRACTION -> GPUImageSphereRefractionFilter()
            FilterType.SWIRL -> GPUImageSwirlFilter()
            FilterType.WEAK_PIXEL_INCLUSION -> GPUImageWeakPixelInclusionFilter()
            FilterType.FALSE_COLOR -> GPUImageFalseColorFilter()
            FilterType.COLOR_BALANCE -> GPUImageColorBalanceFilter()
            FilterType.LEVELS_FILTER_MIN -> GPUImageLevelsFilter()
            FilterType.HALFTONE -> GPUImageHalftoneFilter()
            FilterType.BILATERAL_BLUR -> GPUImageBilateralBlurFilter()
            FilterType.ZOOM_BLUR -> GPUImageZoomBlurFilter()
            FilterType.TRANSFORM2D -> GPUImageTransformFilter()
            FilterType.SOLARIZE -> GPUImageSolarizeFilter()
            FilterType.VIBRANCE -> GPUImageVibranceFilter()
        }
    }

    private fun createBlendFilter(
            context: Context,
            filterClass: Class<out GPUImageTwoInputFilter>
    ): GPUImageFilter {
        return try {
            filterClass.newInstance().apply {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GPUImageFilter()
        }
    }

    private enum class FilterType {
        HORIZONALLIE, EDGESOBEL, RIPPLE, EMBOSS2, BLUR, HOTIMG, ZOOMFAR, FLASHWHITE, ROUNDRECT, CHROMATICSCALE, CHROMATIC, CUBE, DOORWAY, PINWHEEL, CIRCLESCAN, INVERTEDPAGECURL, ROTATIONALBLUR, FAKE3D, SOULSCALE, CONTRAST, GRAYSCALE, SHARPEN, SEPIA, SOBEL_EDGE_DETECTION, THRESHOLD_EDGE_DETECTION, THREE_X_THREE_CONVOLUTION, FILTER_GROUP, EMBOSS, POSTERIZE, GAMMA, BRIGHTNESS, INVERT, HUE, PIXELATION,
        SATURATION, EXPOSURE, HIGHLIGHT_SHADOW, MONOCHROME, OPACITY, RGB, WHITE_BALANCE, VIGNETTE, TONE_CURVE, LUMINANCE, LUMINANCE_THRESHSOLD, BLEND_COLOR_BURN, BLEND_COLOR_DODGE, BLEND_DARKEN,
        BLEND_DIFFERENCE, BLEND_DISSOLVE, BLEND_EXCLUSION, BLEND_SOURCE_OVER, BLEND_HARD_LIGHT, BLEND_LIGHTEN, BLEND_ADD, BLEND_DIVIDE, BLEND_MULTIPLY, BLEND_OVERLAY, BLEND_SCREEN, BLEND_ALPHA,
        BLEND_COLOR, BLEND_HUE, BLEND_SATURATION, BLEND_LUMINOSITY, BLEND_LINEAR_BURN, BLEND_SOFT_LIGHT, BLEND_SUBTRACT, BLEND_CHROMA_KEY, BLEND_NORMAL, LOOKUP_AMATORKA,
        GAUSSIAN_BLUR, CROSSHATCH, BOX_BLUR, CGA_COLORSPACE, DILATION, KUWAHARA, RGB_DILATION, SKETCH, TOON, SMOOTH_TOON, BULGE_DISTORTION, GLASS_SPHERE, HAZE, LAPLACIAN, NON_MAXIMUM_SUPPRESSION,
        SPHERE_REFRACTION, SWIRL, WEAK_PIXEL_INCLUSION, FALSE_COLOR, COLOR_BALANCE, LEVELS_FILTER_MIN, BILATERAL_BLUR, ZOOM_BLUR, HALFTONE, TRANSFORM2D, SOLARIZE, VIBRANCE
    }

    private class FilterList {
        val names: MutableList<String> = LinkedList()
        val filters: MutableList<FilterType> = LinkedList()

        fun addFilter(name: String, filter: FilterType) {
            names.add(name)
            filters.add(filter)
        }
    }

    class FilterAdjuster(filter: GPUImageFilter) {
        private val adjuster: Adjuster<out GPUImageFilter>?

        init {
            adjuster = when (filter) {
                is HorizonalLineFilter -> HorizonalLineAdjuster(filter)
                is EdgeSobelFilter -> EdgeSobelAdjuster(filter)
                is RippleFilter -> RippleAdjuster(filter)
                is EmbossFilter2 -> Emboss2Adjuster(filter)
                is BlurFilter -> BlurAdjuster(filter)
                is HotImgFilter -> HotImgAdjuster(filter)
                is ZoomFarFilter -> ZoomFarAdjuster(filter)
                is FlashWhiteFilter -> FlashWhiteAdjuster(filter)
                is RoundRectFilter -> RoundRectAdjuster(filter)
                is ChromaticScaleFilter -> ChromaticScaleAdjuster(filter)
                is ChromaticFilter -> ChromaticAdjuster(filter)
                is CubeFilter -> CubeAdjuster(filter)
                is DoorWaylFilter -> DoorWayAdjuster(filter)
                is PinWheellFilter -> PinWheelAdjuster(filter)
                is CircleScanFilter -> CircleScanAdjuster(filter)
                is InvertedPageCurlFilter -> InvertedPageCurlAdjuster(filter)
                is RotationBlurFilter -> RotationalBlurAdjuster(filter)
                is GPUImageSharpenFilter -> SharpnessAdjuster(filter)
                is GPUImageSepiaToneFilter -> SepiaAdjuster(filter)
                is GPUImageContrastFilter -> ContrastAdjuster(filter)
                is GPUImageGammaFilter -> GammaAdjuster(filter)
                is GPUImageBrightnessFilter -> BrightnessAdjuster(filter)
                is GPUImageSobelEdgeDetectionFilter -> SobelAdjuster(filter)
                is GPUImageThresholdEdgeDetectionFilter -> ThresholdAdjuster(filter)
                is GPUImage3x3ConvolutionFilter -> ThreeXThreeConvolutionAjuster(filter)
                is GPUImageEmbossFilter -> EmbossAdjuster(filter)
                is GPUImage3x3TextureSamplingFilter -> GPU3x3TextureAdjuster(filter)
                is GPUImageHueFilter -> HueAdjuster(filter)
                is GPUImagePosterizeFilter -> PosterizeAdjuster(filter)
                is GPUImagePixelationFilter -> PixelationAdjuster(filter)
                is GPUImageSaturationFilter -> SaturationAdjuster(filter)
                is GPUImageExposureFilter -> ExposureAdjuster(filter)
                is GPUImageHighlightShadowFilter -> HighlightShadowAdjuster(filter)
                is GPUImageMonochromeFilter -> MonochromeAdjuster(filter)
                is GPUImageOpacityFilter -> OpacityAdjuster(filter)
                is GPUImageRGBFilter -> RGBAdjuster(filter)
                is GPUImageWhiteBalanceFilter -> WhiteBalanceAdjuster(filter)
                is GPUImageVignetteFilter -> VignetteAdjuster(filter)
                is GPUImageLuminanceThresholdFilter -> LuminanceThresholdAdjuster(filter)
                is GPUImageDissolveBlendFilter -> DissolveBlendAdjuster(filter)
                is GPUImageGaussianBlurFilter -> GaussianBlurAdjuster(filter)
                is GPUImageCrosshatchFilter -> CrosshatchBlurAdjuster(filter)
                is GPUImageBulgeDistortionFilter -> BulgeDistortionAdjuster(filter)
                is GPUImageGlassSphereFilter -> GlassSphereAdjuster(filter)
                is GPUImageHazeFilter -> HazeAdjuster(filter)
                is GPUImageSphereRefractionFilter -> SphereRefractionAdjuster(filter)
                is GPUImageSwirlFilter -> SwirlAdjuster(filter)
                is GPUImageColorBalanceFilter -> ColorBalanceAdjuster(filter)
                is GPUImageLevelsFilter -> LevelsMinMidAdjuster(filter)
                is GPUImageBilateralBlurFilter -> BilateralAdjuster(filter)
                is GPUImageTransformFilter -> RotateAdjuster(filter)
                is GPUImageSolarizeFilter -> SolarizeAdjuster(filter)
                is GPUImageVibranceFilter -> VibranceAdjuster(filter)
                else -> null
            }
        }

        fun canAdjust(): Boolean {
            return adjuster != null
        }

        fun adjust(percentage: Int) {
            adjuster?.adjust(percentage)
        }

        private abstract inner class Adjuster<T : GPUImageFilter>(protected val filter: T) {

            abstract fun adjust(percentage: Int)

            protected fun range(percentage: Int, start: Float, end: Float): Float {
                return (end - start) * percentage / 100.0f + start
            }

            protected fun range(percentage: Int, start: Int, end: Int): Int {
                return (end - start) * percentage / 100 + start
            }
        }

        private inner class HorizonalLineAdjuster(filter: HorizonalLineFilter) :
                Adjuster<HorizonalLineFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class EdgeSobelAdjuster(filter: EdgeSobelFilter) :
                Adjuster<EdgeSobelFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class RippleAdjuster(filter: RippleFilter) :
                Adjuster<RippleFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class Emboss2Adjuster(filter: EmbossFilter2) :
                Adjuster<EmbossFilter2>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 5.0f))
            }
        }

        private inner class BlurAdjuster(filter: BlurFilter) :
                Adjuster<BlurFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 10.0f))
            }
        }

        private inner class HotImgAdjuster(filter: HotImgFilter) :
                Adjuster<HotImgFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 10.0f))
            }
        }

        private inner class CircleScanAdjuster(filter: CircleScanFilter) :
                Adjuster<CircleScanFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class ZoomFarAdjuster(filter: ZoomFarFilter) :
                Adjuster<ZoomFarFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setBlurSize(range(percentage, 0.0F, 100.0f))
            }
        }

        private inner class ChromaticAdjuster(filter: ChromaticFilter) :
                Adjuster<ChromaticFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class ChromaticScaleAdjuster(filter: ChromaticScaleFilter) :
                Adjuster<ChromaticScaleFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 1.0F, 2.0f))
            }
        }

        private inner class FlashWhiteAdjuster(filter: FlashWhiteFilter) :
                Adjuster<FlashWhiteFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setAlpha(range(percentage, 0.0F, 1.0f))
            }
        }


        private inner class CubeAdjuster(filter: CubeFilter) :
                Adjuster<CubeFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class RoundRectAdjuster(filter: RoundRectFilter) :
                Adjuster<RoundRectFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRadius(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class DoorWayAdjuster(filter: DoorWaylFilter) :
                Adjuster<DoorWaylFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class PinWheelAdjuster(filter: PinWheellFilter) :
                Adjuster<PinWheellFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class InvertedPageCurlAdjuster(filter: InvertedPageCurlFilter) :
                Adjuster<InvertedPageCurlFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setProgress(range(percentage, 0.0F, 1.0f))
            }
        }

        private inner class RotationalBlurAdjuster(filter: RotationBlurFilter) :
                Adjuster<RotationBlurFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setIntensity(range(percentage, -2.0F, 2.0f))
            }
        }


        private inner class SharpnessAdjuster(filter: GPUImageSharpenFilter) :
                Adjuster<GPUImageSharpenFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setSharpness(range(percentage, -4.0f, 4.0f))
            }
        }

        private inner class PixelationAdjuster(filter: GPUImagePixelationFilter) :
                Adjuster<GPUImagePixelationFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setPixel(range(percentage, 1.0f, 100.0f))
            }
        }

        private inner class HueAdjuster(filter: GPUImageHueFilter) :
                Adjuster<GPUImageHueFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setHue(range(percentage, 0.0f, 360.0f))
            }
        }

        private inner class ContrastAdjuster(filter: GPUImageContrastFilter) :
                Adjuster<GPUImageContrastFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setContrast(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class GammaAdjuster(filter: GPUImageGammaFilter) :
                Adjuster<GPUImageGammaFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setGamma(range(percentage, 0.0f, 3.0f))
            }
        }

        private inner class BrightnessAdjuster(filter: GPUImageBrightnessFilter) :
                Adjuster<GPUImageBrightnessFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setBrightness(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class SepiaAdjuster(filter: GPUImageSepiaToneFilter) :
                Adjuster<GPUImageSepiaToneFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setIntensity(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class SobelAdjuster(filter: GPUImageSobelEdgeDetectionFilter) :
                Adjuster<GPUImageSobelEdgeDetectionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class ThresholdAdjuster(filter: GPUImageThresholdEdgeDetectionFilter) :
                Adjuster<GPUImageThresholdEdgeDetectionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setLineSize(range(percentage, 0.0f, 5.0f))
                filter.setThreshold(0.9f)
            }
        }

        private inner class ThreeXThreeConvolutionAjuster(filter: GPUImage3x3ConvolutionFilter) :
                Adjuster<GPUImage3x3ConvolutionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setConvolutionKernel(
                        floatArrayOf(-1.0f, 0.0f, 1.0f, -2.0f, 0.0f, 2.0f, -1.0f, 0.0f, 1.0f)
                )
            }
        }

        private inner class EmbossAdjuster(filter: GPUImageEmbossFilter) :
                Adjuster<GPUImageEmbossFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.intensity = range(percentage, 0.0f, 4.0f)
            }
        }

        private inner class PosterizeAdjuster(filter: GPUImagePosterizeFilter) :
                Adjuster<GPUImagePosterizeFilter>(filter) {
            override fun adjust(percentage: Int) {
                // In theorie to 256, but only first 50 are interesting
                filter.setColorLevels(range(percentage, 1, 50))
            }
        }

        private inner class GPU3x3TextureAdjuster(filter: GPUImage3x3TextureSamplingFilter) :
                Adjuster<GPUImage3x3TextureSamplingFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class SaturationAdjuster(filter: GPUImageSaturationFilter) :
                Adjuster<GPUImageSaturationFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setSaturation(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ExposureAdjuster(filter: GPUImageExposureFilter) :
                Adjuster<GPUImageExposureFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setExposure(range(percentage, -10.0f, 10.0f))
            }
        }

        private inner class HighlightShadowAdjuster(filter: GPUImageHighlightShadowFilter) :
                Adjuster<GPUImageHighlightShadowFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setShadows(range(percentage, 0.0f, 1.0f))
                filter.setHighlights(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class MonochromeAdjuster(filter: GPUImageMonochromeFilter) :
                Adjuster<GPUImageMonochromeFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setIntensity(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class OpacityAdjuster(filter: GPUImageOpacityFilter) :
                Adjuster<GPUImageOpacityFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setOpacity(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class RGBAdjuster(filter: GPUImageRGBFilter) :
                Adjuster<GPUImageRGBFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRed(range(percentage, 0.0f, 1.0f))
//                filter.setBlue(range(percentage, 0.0f, 1.0f))
//                filter.setGreen(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class WhiteBalanceAdjuster(filter: GPUImageWhiteBalanceFilter) :
                Adjuster<GPUImageWhiteBalanceFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setTemperature(range(percentage, 2000.0f, 8000.0f))
            }
        }

        private inner class VignetteAdjuster(filter: GPUImageVignetteFilter) :
                Adjuster<GPUImageVignetteFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setVignetteStart(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class LuminanceThresholdAdjuster(filter: GPUImageLuminanceThresholdFilter) :
                Adjuster<GPUImageLuminanceThresholdFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setThreshold(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class DissolveBlendAdjuster(filter: GPUImageDissolveBlendFilter) :
                Adjuster<GPUImageDissolveBlendFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setMix(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class GaussianBlurAdjuster(filter: GPUImageGaussianBlurFilter) :
                Adjuster<GPUImageGaussianBlurFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setBlurSize(range(percentage, 0.0f, 10f))
            }
        }

        private inner class CrosshatchBlurAdjuster(filter: GPUImageCrosshatchFilter) :
                Adjuster<GPUImageCrosshatchFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setCrossHatchSpacing(range(percentage, 0.0f, 0.06f))
                filter.setLineWidth(range(percentage, 0.0f, 0.006f))
            }
        }

        private inner class BulgeDistortionAdjuster(filter: GPUImageBulgeDistortionFilter) :
                Adjuster<GPUImageBulgeDistortionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRadius(range(percentage, 0.0f, 1.0f))
                filter.setScale(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class GlassSphereAdjuster(filter: GPUImageGlassSphereFilter) :
                Adjuster<GPUImageGlassSphereFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class HazeAdjuster(filter: GPUImageHazeFilter) :
                Adjuster<GPUImageHazeFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setDistance(range(percentage, -0.3f, 0.3f))
                filter.setSlope(range(percentage, -0.3f, 0.3f))
            }
        }

        private inner class SphereRefractionAdjuster(filter: GPUImageSphereRefractionFilter) :
                Adjuster<GPUImageSphereRefractionFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class SwirlAdjuster(filter: GPUImageSwirlFilter) :
                Adjuster<GPUImageSwirlFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setAngle(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ColorBalanceAdjuster(filter: GPUImageColorBalanceFilter) :
                Adjuster<GPUImageColorBalanceFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setMidtones(
                        floatArrayOf(
                                range(percentage, 0.0f, 1.0f),
                                range(percentage / 2, 0.0f, 1.0f),
                                range(percentage / 3, 0.0f, 1.0f)
                        )
                )
            }
        }

        private inner class LevelsMinMidAdjuster(filter: GPUImageLevelsFilter) :
                Adjuster<GPUImageLevelsFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f)
            }
        }

        private inner class BilateralAdjuster(filter: GPUImageBilateralBlurFilter) :
                Adjuster<GPUImageBilateralBlurFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setDistanceNormalizationFactor(range(percentage, 0.0f, 15.0f))
            }
        }

        private inner class RotateAdjuster(filter: GPUImageTransformFilter) :
                Adjuster<GPUImageTransformFilter>(filter) {
            override fun adjust(percentage: Int) {
                val transform = FloatArray(16)
                Matrix.setRotateM(transform, 0, (360 * percentage / 100).toFloat(), 0f, 0f, 1.0f)
                filter.transform3D = transform
            }
        }

        private inner class SolarizeAdjuster(filter: GPUImageSolarizeFilter) :
                Adjuster<GPUImageSolarizeFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setThreshold(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class VibranceAdjuster(filter: GPUImageVibranceFilter) :
                Adjuster<GPUImageVibranceFilter>(filter) {
            override fun adjust(percentage: Int) {
                filter.setVibrance(range(percentage, -1.2f, 1.2f))
            }
        }
    }
}
