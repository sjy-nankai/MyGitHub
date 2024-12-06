package com.android.mygithub.mock

import com.android.github.domain.model.Repository
import com.android.github.domain.model.RepositoryOwner


val mockToken = "test-token"
val mockErrorMessage = "Test exception"

val mockRepos = listOf(
    Repository(
        id = 1,
        name = "repo1",
        fullName = "name1",
        description = "description1",
        language = "kotlin",
        starCount = 1,
        forkCount = 1,
        owner = RepositoryOwner(
            id = 1,
            login = "user-name-1",
            avatarUrl = "http://avtarurl.com/1"
        ),
        updatedAt = "2013-01-05T17:58:47Z"
    ),
    Repository(
        id = 2,
        name = "repo2",
        fullName = "name2",
        description = "description2",
        language = "java",
        starCount = 2,
        forkCount = 2,
        owner = RepositoryOwner(
            id = 2,
            login = "user-name-2",
            avatarUrl = "http://avtarurl.com/2"
        ),
        updatedAt = "2013-01-05T17:58:47Z"
    )

)