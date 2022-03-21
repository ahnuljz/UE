# UE
This public respository is about the detailed processing codes and data for the paper:

**Impact of urbanization on eco-environmental quality presents an inverted U-curve in the Yangtze River Delta, China**


#### 1. How to start:

Before starting, the folders of the respository is described here as follows:
- Environment
    * The javascript codes for processing remote sensing data on GEE(Google earth engine) platform.
    * The RSEI modeling code is located in URL: https://code.earthengine.google.com/?accept_repo=users/leejianzhou2080/rsei
- Urbanization
    * The python codes and data for constructing the urbanization index.
    * There is a special java project based on springboot to handle the messy source data from yearbooks, and the source EXCEL tables is located in folder: /Urbanization/City/src/main/resources/static/city
- CCD
    * The novel model presented in the paper called: a standard Euclidean distance based model for coupling coordinated degree.
    * The model above is implemented by python in this folder.
- ARIMA
    * A model for predicting urbanization for the future 10 years from the past 36 years implemented by python.
  
#### 2. Prerequisites reminder:

In order to run these codes, you'd better know the following introductory knowledge:
- python + jupyter notebook
- java + maven + springboot
- javascript + GEE API