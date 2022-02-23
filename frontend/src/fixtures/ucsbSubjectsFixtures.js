const ucsbSubjectsFixtures = {
    
    /**
     * private long id;

    private String subjectCode;
    private String subjectTranslation;
    private String deptCode;
    private String collegeCode;
    private String relatedDeptCode;
    private boolean inactive;
     */
    oneSubject: {
        "id": 1,
        "subjectCode": "20221",
        "subjectTranslation": "Noon on January 2nd",
        "deptCode": "2022-01-02T12:00:00",
        "collegeCode": "",
        "relatedDeptCode": "",
        "inactive": false
    },
    threeSubjectCodes: [
        {
            "id": 1,
            "subjectCode": "20221",
            "subjectTranslation": "Noon on January 2nd",
            "deptCode": "2022-01-02T12:00:00",
            "collegeCode": "",
            "relatedDeptCode": "",
            "inactive": false
        },
        {
            "id": 2,
            "subjectCode": "20321",
            "subjectTranslation": "Noon on January 3rd",
            "deptCode": "2022-01-03T12:00:00",
            "collegeCode": "",
            "relatedDeptCode": "",
            "inactive": false
        },
        {
            "id": 3,
            "subjectCode": "23121",
            "subjectTranslation": "Noon on January 4th",
            "deptCode": "2022-01-04T12:00:00",
            "collegeCode": "",
            "relatedDeptCode": "",
            "inactive": false
        },
    ]
};


export { ucsbSubjectsFixtures };

