package ru.shtykin.testappchat.settings

interface ProfileStore {
    var name: String
    var username: String
    var birthday: String
    var city: String
    var avatar: String
    var avatarUrl: String
    var status: String
}