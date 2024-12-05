package com.android.github.common

const val clientId = "Ov23liBhyNZh7bX1xGm6"
const val redirectUri = "mygithub://oauth/callback"
const val clientSecret = "493549cbeaf0711a7b5084109e3223c1ff98c2c8"
const val scope = "user,repo"  // 请求的权限范围，可以根据需要调整
const val authUrl = "https://github.com/login/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUri&scope=$scope"