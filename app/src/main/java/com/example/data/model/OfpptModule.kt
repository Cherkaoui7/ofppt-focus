package com.example.data.model

data class OfpptModule(
    val name: String,
    val description: String,
    val iconName: String, // e.g. "React", "Laravel"
    val colorHex: String, // color represent
    val coreTopics: List<NoteTopic>,
    val studyResources: List<StudyResource>,
    val defaultAssignments: List<DefaultAssignment>
)

data class NoteTopic(
    val title: String,
    val content: String
)

data class StudyResource(
    val title: String,
    val type: String, // "DOC", "VIDEO", "TUTORIAL"
    val url: String
)

data class DefaultAssignment(
    val title: String,
    val description: String,
    val points: Int
)
