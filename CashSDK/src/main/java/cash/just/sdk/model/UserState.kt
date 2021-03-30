package cash.just.sdk.model

enum class UserState {
    GUEST,
    KYC_NOT_VERIFIED,
    KYC_VERIFIED,
    NOT_VALID,
    LOGGED_IN,
    KYC_VERIFYING
}