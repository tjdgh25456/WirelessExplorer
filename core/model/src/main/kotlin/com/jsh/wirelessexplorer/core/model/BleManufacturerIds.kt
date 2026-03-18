package com.jsh.wirelessexplorer.core.model

/**
 * Bluetooth SIG 등록 Company Identifier → 회사명 매핑.
 * https://www.bluetooth.com/specifications/assigned-numbers/company-identifiers/
 */
object BleManufacturerIds {
    val names: Map<Int, String> = mapOf(
        0x0001 to "Nokia",
        0x0002 to "Intel",
        0x0006 to "Microsoft",
        0x000A to "Qualcomm Technologies International",
        0x000D to "Texas Instruments",
        0x000F to "Broadcom",
        0x001D to "Qualcomm",
        0x0046 to "MediaTek",
        0x004C to "Apple",
        0x0059 to "Nordic Semiconductor",
        0x0075 to "Samsung Electronics",
        0x0078 to "Nike",
        0x0087 to "Garmin",
        0x00D2 to "Dialog Semiconductor",
        0x00E0 to "Google",
        0x010F to "Xiaomi",
        0x0131 to "Huawei",
        0x0154 to "Cypress Semiconductor",
        0x0157 to "Anhui Huami (Amazfit)",
        0x0171 to "Amazon",
        0x018D to "Realtek",
        0x01A7 to "LEGO System",
        0x0201 to "Blue Clover Devices",
        0x02E5 to "Fitbit",
        0x0310 to "Bose",
        0x0376 to "LG Electronics",
        0x038F to "Shenzhen Goodix",
        0x0397 to "OnePlus",
        0x0499 to "Ruuvi Innovations",
        0x0550 to "Sony",
        0x0822 to "Tile",
    )
}
