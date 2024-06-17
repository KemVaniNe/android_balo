package com.example.balo.data.model.enum

enum class NotificationFromAdmin(val value: String) {
    cancel("Đơn hàng của bạn đã bị hủy!!"),
    accept("Đơn hàng của bạn đã được xác nhận!"),
    pack("Đơn hàng của đang được đóng gói!"),
    ship("Đơn hàng của bạn đang được vận chuyển!"),
    success("Đơn hàng của bạn đã được hoàn thành! Hãy đánh giá 5 sao để ủng hộ chúng tôi"),
}

enum class NotificationFromClient(val value: String) {
    cancel("Đơn hàng của bạn đã bị hủy!!"),
    new("Bạn đã có đơn hàng mới!!"),
}

enum class Notification(val property: String) {
    idUser("name"),
    idOrder("idorder"),
    notification("notification"),
    datatime("datetime"),
    isSeen("isSeen"),
    role("role")
}