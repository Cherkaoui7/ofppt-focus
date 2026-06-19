package com.example.data.provider

import com.example.data.model.OfpptModule
import com.example.data.model.NoteTopic
import com.example.data.model.StudyResource
import com.example.data.model.DefaultAssignment

object OfpptModuleProvider {
    val modules = listOf(
        OfpptModule(
            name = "React",
            description = "Master robust, interactive front-end application architectures with component lifecycle management and hooks.",
            iconName = "React",
            colorHex = "#00D8FF",
            coreTopics = listOf(
                NoteTopic(
                    "State Management in React",
                    "State represents the local data of a component. In functional React components, the useState hook is used to manage locally declared state. Every time state variable changes via its state-updater function (e.g. setState), React automatically re-renders that component to keep the interface in sync."
                ),
                NoteTopic(
                    "Understanding React Hooks",
                    "Hooks are functions that let functional components hook into React's state management and lifecycle features. Popular hooks include useState (state management), useEffect (fetching data, custom side-effects), useContext (consuming global context), and useMemo (caching expensive calculations)."
                ),
                NoteTopic(
                    "Component Cycles (useEffect)",
                    "The useEffect hook accepts a callback function and a dependencies array. It lets you synchronize a component with external systems. If the dependency array is empty [], it runs once when the component mounts. If it has values (e.g. [someValue]), the callback runs whenever those dependencies change."
                )
            ),
            studyResources = listOf(
                StudyResource("React Official Documentation", "DOC", "https://react.dev"),
                StudyResource("React Hooks Video Course", "VIDEO", "https://react.dev/reference/react"),
                StudyResource("OFPPT Front-End Best Practices", "TUTORIAL", "https://react.dev/learn/thinking-in-react")
            ),
            defaultAssignments = listOf(
                DefaultAssignment("Interactive Task Board SPA", "Build a high-performance, responsive React task board using local storage, custom filters, and drag-and-drop simulated hooks.", 50),
                DefaultAssignment("Multi-Step Wizard Form", "Implement a multi-step student registration wizard form featuring robust field validation and global Context state sharing.", 75)
            )
        ),
        OfpptModule(
            name = "Laravel",
            description = "Build secure, enterprise-grade MVC backend services, API interfaces, and persistent SQLite storage databases.",
            iconName = "Laravel",
            colorHex = "#FF2D20",
            coreTopics = listOf(
                NoteTopic(
                    "Model-View-Controller (MVC)",
                    "Laravel enforces the MVC software architecture pattern. Models handle database tables and Eloquent ORM relationships. Views (Blade files) manage user-facing layouts, and Controllers direct HTTP traffic request routing, fetching from database models and returning views or raw JSON feeds."
                ),
                NoteTopic(
                    "Eloquent ORM Active Record",
                    "Eloquent is Laravel's powerful relational database Object-Relational Mapper. Each DB table has a matching Model (e.g. 'users' table -> User model). Eloquent lets you interact with database records as standard objects and write fluent, self-explanatory SQL statements."
                ),
                NoteTopic(
                    "Migrations & Seeders",
                    "Database migrations act like version control for table layouts. Developers use them to safely share table properties in teams. Seeders are PHP scripts used to pre-load database tables with default static fields or random fake records for development."
                )
            ),
            studyResources = listOf(
                StudyResource("Laravel Documentation", "DOC", "https://laravel.com/docs"),
                StudyResource("Laracasts Free Learning Hub", "VIDEO", "https://laracasts.com"),
                StudyResource("RESTful API Architecture", "TUTORIAL", "https://laravel.com/docs/routing")
            ),
            defaultAssignments = listOf(
                DefaultAssignment("Student API Scaffold Controller", "Scaffold a complete CRUD API controller endpoint for a student profile repository containing form field validations.", 40),
                DefaultAssignment("Database Migration & Seeding", "Write a collection of related database migrations for courses, enrollment receipts, and seed fake students using Eloquent.", 60)
            )
        ),
        OfpptModule(
            name = "MongoDB",
            description = "Store and aggregate documents dynamically in agile, horizontal-scaling NoSQL MongoDB collections.",
            iconName = "MongoDB",
            colorHex = "#47A248",
            coreTopics = listOf(
                NoteTopic(
                    "Document Databases (NoSQL)",
                    "Unlike SQL rows, NoSQL databases like MongoDB save records as highly flexible, JSON-like document schema (BSON format). This schema-less design lets developers insert documents with custom fields dynamically without executing structural migrations."
                ),
                NoteTopic(
                    "Internal BSON Encoding",
                    "MongoDB stores JSON documents in a secure, efficient serialized, binary format called BSON (Binary JSON). BSON supports more advanced data types like Date objects, Int32/Int64 formats, and raw binary streams, facilitating fast database reads."
                ),
                NoteTopic(
                    "Aggregation Framework Pipelines",
                    "The aggregation framework executes complex data analytics using multi-stage pipelines. Programmers pipe datasets through consecutive filters such as \$match (filtering), \$group (aggregating categories), and \$sort (arranging) to generate reports."
                )
            ),
            studyResources = listOf(
                StudyResource("MongoDB Manual", "DOC", "https://www.mongodb.com/docs/ manual/"),
                StudyResource("MongoDB University Courses", "VIDEO", "https://learn.mongodb.com/"),
                StudyResource("NoSQL Design Patterns Guide", "TUTORIAL", "https://www.mongodb.com/developer/products/mongodb/schema-design/")
            ),
            defaultAssignments = listOf(
                DefaultAssignment("NoSQL School Library Directory", "Formulate a document strategy for tracking books, check-out rentals, and index records efficiently by ISBN tags.", 45),
                DefaultAssignment("Aggregation Pipeline Report", "Write a 3-stage Aggregation pipeline finding average test scores filtered by graduation class directories.", 70)
            )
        ),
        OfpptModule(
            name = "Cloud",
            description = "Explore flexible, modern deployment options, container orchestration, and elastic compute instances on cloud platforms.",
            iconName = "Cloud",
            colorHex = "#0052CC",
            coreTopics = listOf(
                NoteTopic(
                    "IaaS, PaaS, and SaaS Services",
                    "Cloud services are cataloged into three core paradigms: Infrastructure-as-a-Service (IaaS - e.g. EC2 for bare servers), Platform-as-a-Service (PaaS - e.g. Heroku, AppEngine for instant deployment pipelines), and Software-as-a-Service (SaaS - e.g. Google Drive)."
                ),
                NoteTopic(
                    "Docker Containers & Isolation",
                    "Docker packages code and custom operating system dependencies together into light isolated virtual environments called Containers. This ensures compiled course applications execute on every staging server without matching environment errors."
                ),
                NoteTopic(
                    "Serverless Architecture benefits",
                    "Serverless execution models (e.g. AWS Lambda, Google Cloud Functions) let developers execute compute scripts on-demand. Cloud providers handle absolute server auto-scaling, billing purely for code run milliseconds, rather than static monthly costs."
                )
            ),
            studyResources = listOf(
                StudyResource("AWS Academy Tutorials", "DOC", "https://aws.amazon.com/training/"),
                StudyResource("Google Cloud Architecture Guide", "DOC", "https://cloud.google.com/docs"),
                StudyResource("Docker Container Setup Video", "VIDEO", "https://docs.docker.com/get-started/")
            ),
            defaultAssignments = listOf(
                DefaultAssignment("Dockerize Node App", "Write a compact Dockerfile to package a simple Node.js REST app securely and deploy utilizing local environment fields.", 50),
                DefaultAssignment("Cloud Budgeting Architecture", "Design an auto-scaling cloud compute diagram and calculate average monthly costs for 1000 daily active users.", 60)
            )
        ),
        OfpptModule(
            name = "UML",
            description = "Design cohesive software blueprints, architectural packages, and entity interactions using UML standards.",
            iconName = "UML",
            colorHex = "#EA7500",
            coreTopics = listOf(
                NoteTopic(
                    "Unified Modeling Language (UML)",
                    "UML is a standardized visual modeling grammar utilized across software engineering to draft blueprint layouts outlining user interactions (Use Case), data definitions (Class), and operational sequences."
                ),
                NoteTopic(
                    "Class Diagrams and Relationships",
                    "Class diagrams are static drawings representing application classes, attributes, methods, and relationships. Relationships are modeled using arrows: Inheritance (hollow arrow), Association (solid line), and Composition (filled diamond)."
                ),
                NoteTopic(
                    "Sequence Diagrams flow",
                    "Sequence diagrams detail behavior and operations by graphing chronological interactions between software elements. It shows messages passed across objects along vertical lifetime columns called lifelines."
                )
            ),
            studyResources = listOf(
                StudyResource("UML Distilled Ebook Guidelines", "DOC", "https://www.uml-diagrams.org/"),
                StudyResource("Sequence Diagrams Tutorial", "VIDEO", "https://www.visual-paradigm.com/guide/uml-unified-modeling-language/"),
                StudyResource("System Modeling Standards", "TUTORIAL", "https://www.uml.org/")
            ),
            defaultAssignments = listOf(
                DefaultAssignment("E-Commerce Class Architecture", "Draft a complete dynamic Class diagram containing products, cart items, secure checkouts, and clean inheritance paths.", 55),
                DefaultAssignment("Student Register Use-Case Flow", "Write a sequence diagram modeling interactive login validations, database lookups, and security session generations.", 65)
            )
        ),
        OfpptModule(
            name = "Agile",
            description = "Deliver constant project value, adapt dynamically to client feedback, and organize workflows using Scrum/Kanban frameworks.",
            iconName = "Agile",
            colorHex = "#800080",
            coreTopics = listOf(
                NoteTopic(
                    "The Agile Manifesto pillars",
                    "Agile development practices center on four key pillars: Value individuals and interactions over tools, deploy working software rapidly, participate in close customer collaboration, and embrace change immediately."
                ),
                NoteTopic(
                    "Scrum Framework sprints",
                    "Scrum structures product development into short fixed intervals (typically 2-4 weeks) called Sprints. Daily Standups track progress, Sprint Planning prepares goals, and Retrospectives optimize developer workflows."
                ),
                NoteTopic(
                    "Kanban Task Boards limits",
                    "Kanban uses a visual grid to coordinate the flow of development, splitting operations into 'To Do', 'In Progress', and 'Done'. Teams enforce WIP (Work In Progress) limits to prevent bottlenecks and system overwork."
                )
            ),
            studyResources = listOf(
                StudyResource("Agile Alliance Guide", "DOC", "https://www.agilealliance.org/agile101/"),
                StudyResource("Scrum Framework Video Course", "VIDEO", "https://www.scrum.org/"),
                StudyResource("Understanding Kanban WIP limits", "TUTORIAL", "https://www.atlassian.com/agile/kanban")
            ),
            defaultAssignments = listOf(
                DefaultAssignment("Draft Scrum Product Backlog", "Prepare a styled product backlog sheet with custom estimation points and define exact acceptance conditions for user stories.", 40),
                DefaultAssignment("Sprint Review Retrospective Brief", "Synthesize a professional mock Sprint Review documenting speed metrics, feedback iterations, and next priority columns.", 60)
            )
        )
    )

    fun getModuleByName(name: String): OfpptModule? {
        return modules.find { it.name.lowercase() == name.lowercase() }
    }
}
