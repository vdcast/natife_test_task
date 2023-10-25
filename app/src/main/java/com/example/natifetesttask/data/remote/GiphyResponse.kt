package com.example.natifetesttask.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiphyResponse(
    val data: List<Data>,
    val pagination: Pagination,
    val meta: Meta
)

@Serializable
data class Analytics(
    val onload: Onload,
    val onclick: Onclick,
    val onsent: Onsent
)

@Serializable
data class Data(
    val type: String,
    val id: String,
    val url: String,
    val slug: String,
    @SerialName("bitly_gif_url") val bitlyGifUrl: String,
    @SerialName("bitly_url") val bitlyUrl: String,
    @SerialName("embed_url") val embedUrl: String,
    val username: String,
    val source: String,
    val title: String,
    val rating: String,
    @SerialName("content_url") val contentUrl: String,
    @SerialName("source_tld") val sourceTld: String,
    @SerialName("source_post_url") val sourcePostUrl: String,
    @SerialName("is_sticker") val isSticker: Int,
    @SerialName("import_datetime") val importDatetime: String,
    @SerialName("trending_datetime") val trendingDatetime: String,
    val images: Images,
    val user: User? = null,
    @SerialName("analytics_response_payload") val analyticsResponsePayload: String,
    val analytics: Analytics,
    val cta: Cta? = null,
)

@Serializable
data class Cta(
    val link: String,
    val text: String? = null,

)
@Serializable
data class FixedHeight(
    val height: Int,
    val width: Int,
    val size: Int,
    val url: String,
    @SerialName("mp4_size") val mp4Size: Int? = null,
    val mp4: String? = null,
    @SerialName("webp_size") val webpSize: Int? = null,
    val webp: String? = null,
)

@Serializable
data class FixedHeightDownsampled(
    val height: Int,
    val width: Int,
    val size: Int,
    val url: String,
    @SerialName("webp_size") val webpSize: Int? = null,
    val webp: String? = null,
)

@Serializable
data class FixedHeightSmall(
    val height: Int,
    val width: Int,
    val size: Int,
    val url: String,
    @SerialName("mp4_size") val mp4Size: Int? = null,
    val mp4: String? = null,
    @SerialName("webp_size") val webpSize: Int? = null,
    val webp: String? = null,
)

@Serializable
data class FixedWidth(
    val height: Int,
    val width: Int,
    val size: Int,
    val url: String,
    @SerialName("mp4_size") val mp4Size: Int? = null,
    val mp4: String? = null,
    @SerialName("webp_size") val webpSize: Int? = null,
    val webp: String? = null,
)

@Serializable
data class FixedWidthDownsampled(
    val height: Int,
    val width: Int,
    val size: Int,
    val url: String,
    @SerialName("webp_size") val webpSize: Int? = null,
    val webp: String? = null,
)

@Serializable
data class FixedWidthSmall(
    val height: Int,
    val width: Int,
    val size: Int,
    val url: String,
    @SerialName("mp4_size") val mp4Size: Int? = null,
    val mp4: String? = null,
    @SerialName("webp_size") val webpSize: Int? = null,
    val webp: String? = null,
)

@Serializable
data class Images(
    val original: Original,
    @SerialName("fixed_height") val fixedHeight: FixedHeight? = null,
    @SerialName("fixed_height_downsampled") val fixedHeightDownsampled: FixedHeightDownsampled? = null,
    @SerialName("fixed_height_small") val fixedHeightSmall: FixedHeightSmall? = null,
    @SerialName("fixed_width") val fixedWidth: FixedWidth? = null,
    @SerialName("fixed_width_downsampled") val fixedWidthDownsampled: FixedWidthDownsampled? = null,
    @SerialName("fixed_width_small") val fixedWidthSmall: FixedWidthSmall? = null
)

@Serializable
data class Meta(
    val status: Int,
    val msg: String,
    @SerialName("response_id") val responseId: String
)

@Serializable
data class Onclick(
    val url: String
)

@Serializable
data class Onload(
    val url: String
)

@Serializable
data class Onsent(
    val url: String
)

@Serializable
data class Original(
    val height: Int,
    val width: Int,
    val size: Int,
    val url: String,
    @SerialName("mp4_size") val mp4Size: Int? = null,
    val mp4: String? = null,
    @SerialName("webp_size") val webpSize: Int? = null,
    val webp: String? = null,
    val frames: Int,
    val hash: String
)

@Serializable
data class Pagination(
    @SerialName("total_count") val totalCount: Int,
    val count: Int,
    val offset: Int
)

@Serializable
data class User(
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("banner_image") val bannerImage: String,
    @SerialName("banner_url") val bannerUrl: String,
    @SerialName("profile_url") val profileUrl: String,
    val username: String,
    @SerialName("display_name") val displayName: String,
    val description: String,
    @SerialName("instagram_url") val instagramUrl: String,
    @SerialName("website_url") val websiteUrl: String,
    @SerialName("is_verified") val isVerified: Boolean
)
