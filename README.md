# yelp_reviews-api-java
RESTful API for Milwaukee Ale House Restaurant Review Using Yelp and Google Vision API


**Curl Command:**
```curl
curl --location --request GET 'localhost:8080/yelp/aleHouse/reviews?locale=en_PH'
```

**Response:**
```json
{
    "reviews": [
        {
            "id": "myVyIMFYj-6UrLyoK4DYhw",
            "url": "https://www.yelp.com/biz/milwaukee-ale-house-milwaukee?adjust_creative=9bpn8CEy69vXX__kLpifFQ&hrid=myVyIMFYj-6UrLyoK4DYhw&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_reviews&utm_source=9bpn8CEy69vXX__kLpifFQ",
            "text": "I'm glad this place is opened on Labor Day although with shortened hours. I got an interesting ale infused with tea. I didn't really read the description...",
            "rating": 4,
            "time_created": "2020-09-07 13:04:12",
            "user": {
                "id": "sq-kGYqe7k_75uysJ8RwOQ",
                "profile_url": "https://www.yelp.com/user_details?userid=sq-kGYqe7k_75uysJ8RwOQ",
                "image_url": "https://s3-media3.fl.yelpcdn.com/photo/8I8Vetcy_ABdrO5QTXz86w/o.jpg",
                "name": "Abigail P.",
                "emotions": [
                    {
                        "joyLikelihood": "VERY_UNLIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    }
                ]
            }
        },
        {
            "id": "ZUoA2BMe5YbRV7tFc69_mQ",
            "url": "https://www.yelp.com/biz/milwaukee-ale-house-milwaukee?adjust_creative=9bpn8CEy69vXX__kLpifFQ&hrid=ZUoA2BMe5YbRV7tFc69_mQ&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_reviews&utm_source=9bpn8CEy69vXX__kLpifFQ",
            "text": "Really good beers and food. The wings and pulled pork are faves. They also created their own seltzer that is REALLY good. Tons of outdoor seating, the...",
            "rating": 4,
            "time_created": "2020-07-04 10:35:05",
            "user": {
                "id": "BaQbpZodzfYMEyTX1YTdwA",
                "profile_url": "https://www.yelp.com/user_details?userid=BaQbpZodzfYMEyTX1YTdwA",
                "image_url": "https://s3-media1.fl.yelpcdn.com/photo/IxaGnnxXdBRFNeHQOYg3Jg/o.jpg",
                "name": "Lauren F.",
                "emotions": [
                    {
                        "joyLikelihood": "VERY_LIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    },
                    {
                        "joyLikelihood": "VERY_LIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    },
                    {
                        "joyLikelihood": "VERY_UNLIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    },
                    {
                        "joyLikelihood": "VERY_UNLIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    },
                    {
                        "joyLikelihood": "VERY_UNLIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    },
                    {
                        "joyLikelihood": "VERY_UNLIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    }
                ]
            }
        },
        {
            "id": "aJHjuWVleKXUzyuh1bVqIw",
            "url": "https://www.yelp.com/biz/milwaukee-ale-house-milwaukee?adjust_creative=9bpn8CEy69vXX__kLpifFQ&hrid=aJHjuWVleKXUzyuh1bVqIw&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_reviews&utm_source=9bpn8CEy69vXX__kLpifFQ",
            "text": "Came here during Covid and had drinks and dinner on their patio. The servers were so attentive! Had the Cheese Curds which were fabulous, Ale House Burger...",
            "rating": 5,
            "time_created": "2020-06-19 11:50:18",
            "user": {
                "id": "OagJ60oRV3mmyiCDfv32Yw",
                "profile_url": "https://www.yelp.com/user_details?userid=OagJ60oRV3mmyiCDfv32Yw",
                "image_url": "https://s3-media4.fl.yelpcdn.com/photo/uMk8odkAx_MojVtkzsINGg/o.jpg",
                "name": "Erica Z.",
                "emotions": [
                    {
                        "joyLikelihood": "VERY_LIKELY",
                        "sorrowLikelihood": "VERY_UNLIKELY"
                    }
                ]
            }
        }
    ]
}
```
