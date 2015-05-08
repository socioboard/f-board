//
//  PostViewController.m
//  TwitterBoard
//
//  Created by Sumit on 21/04/15.
//  All rights reserved.
//

#import "PostViewController.h"
#import "SUCache.h"
#import "PostScheduleController.h"
#import "SingletonClass.h"
#import <FacebookSDK/FacebookSDK.h>
#import <MobileCoreServices/MobileCoreServices.h>
#import "UIImageView+WebCache.h"
#define FacebookTitle @"OpenStoryFacebookTitle"
#define FacebookType @"OpenStoryFacebookType"
#define FacebookDescription @"OpenStoryFacebookDescription"
#define FacebookActionType @"OpenStoryAction"
@implementation PostViewController
@synthesize _selectedDate,sharePostBtn;

- (void)viewDidLoad
{
    [super viewDidLoad];
//    self.view.backgroundColor = [UIColor colorWithRed:(CGFloat)23/255 green:(CGFloat)142/255 blue:(CGFloat)210/255 alpha:1];
    
    self.view.backgroundColor = [UIColor whiteColor];
   

    self.postImgButton =  [UIButton buttonWithType:UIButtonTypeCustom];
    self.postImgButton.frame = CGRectMake(20, 90, 100, 130);
    [self.postImgButton setImage:[UIImage imageNamed:@"photo.png"]  forState:UIControlStateNormal] ;
    [self.postImgButton addTarget:self
                           action:@selector(pickFromLibrary:)
                 forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.postImgButton];

    
    self.textPostButton =  [UIButton buttonWithType:UIButtonTypeCustom];
    self.textPostButton.frame = CGRectMake(200, 90, 100, 130);
    [self.textPostButton setImage:[UIImage imageNamed:@"text.png"]  forState:UIControlStateNormal] ;
    [self.textPostButton addTarget:self
                           action:@selector(textPostButton:)
                 forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.textPostButton];

    self.videoPostButton =  [UIButton buttonWithType:UIButtonTypeCustom];
    self.videoPostButton.frame = CGRectMake(200, 250, 100, 130);
    [self.videoPostButton setImage:[UIImage imageNamed:@"video.png"]  forState:UIControlStateNormal] ;
    [self.videoPostButton addTarget:self
                            action:@selector(VideoPostButton:)
                  forControlEvents:UIControlEventTouchUpInside];
        //[self.view addSubview:self.videoPostButton];

    
    // Do any additional setup after loading the view.
    sharePostBtn = [[UIButton alloc]init];
    sharePostBtn.frame = CGRectMake(200, 90, 100, 130);
    [sharePostBtn setImage:[UIImage imageNamed:@"event.png"]  forState:UIControlStateNormal] ;
//    sharePostBtn.backgroundColor = [UIColor colorWithRed:(CGFloat)70/255 green:(CGFloat)98/255 blue:(CGFloat)158/255 alpha:1];
    [sharePostBtn addTarget:self
                     action:@selector(eventPostButton:)
           forControlEvents:UIControlEventTouchUpInside];
        // [self.view addSubview:sharePostBtn];
    
}

-(void)VideoPostButton:(UIButton *)button{

}


-(void)textPostButton:(UIButton *)button{
    PostScheduleController *postSchedule=[[PostScheduleController alloc] init];
    postSchedule.type=@"Text";
    postSchedule.title=@"Schedule Post";
  [self.navigationController pushViewController:postSchedule animated:YES];
}



-(void)eventPostButton:(UIButton *)button{

}

//- (void)setupLocalNotifications:(NSString *)seconds {
//    
//    
//    NSString *userId = @"265952946944590";
//    
//    self.localNotification = [[UILocalNotification alloc] init];
//    if (self.pageTextField.text) {
//        self.localNotification.alertBody = self.pageTextField.text;
//    }else{
//        self.localNotification.alertBody = @"Reminder to post";
//    }
//    
//    time_t unixTime = (time_t) [[NSDate date] timeIntervalSince1970];
//    NSNumber * numbToStore=[NSNumber numberWithInteger:unixTime];
//    NSMutableDictionary * dictMessage=[[NSMutableDictionary alloc]init];
//    [dictMessage setObject:self.localNotification.alertBody forKey:@"Text"];
//    [dictMessage setObject:seconds forKey:@"TimeStamp"];
//    [dictMessage setObject:userId forKey:@"UserId"];
//    if (self.mediaCheck) {
//        [dictMessage setObject:@"true" forKey:@"image"];
//    }else{
//        [dictMessage setObject:@"false" forKey:@"image"];
//    }
//    
//    
//    NSLog(@"notification body %@ and time stamp %@ and userId %@",self.localNotification.alertBody,numbToStore,userId);
//    
//    
//    [self.localNotification setUserInfo:dictMessage];
//    NSDate *now = [NSDate date];
//
////    NSDate *dateToFire = [now dateByAddingTimeInterval:[seconds doubleValue]];
//    
//    NSDate *dateToFire = [now dateByAddingTimeInterval:10];
//
//    NSLog(@"fire time: %@", dateToFire);
//    self.localNotification.fireDate = dateToFire;
//    self.localNotification.soundName = UILocalNotificationDefaultSoundName;
//    
//    [[UIApplication sharedApplication] scheduleLocalNotification: self.localNotification];
//    
//}


//- (void)date:(id)sender {
//    
//    datepicker=[[UIDatePicker alloc] initWithFrame:CGRectMake(0, 250, 325, 300)];
//    datepicker.datePickerMode = UIDatePickerModeDateAndTime;
//    datepicker.hidden = NO;
//    datepicker.date = [NSDate date];
//    
//    [datepicker addTarget:self
//                   action:@selector(LabelChange:)
//         forControlEvents:UIControlEventValueChanged];
//    [self.view addSubview:datepicker]; //this can set value of selected date to your label change according to your condition
//    
//    
//    NSDateFormatter * df = [[NSDateFormatter alloc] init];
//    [df setDateFormat:@"MM-dd-yyyy HH:mm:ss"]; // from here u can change format..
//    
//    _selectedDate = [[UILabel alloc]init];
//    _selectedDate.frame=CGRectMake(100, 150, 250, 50);
//        //[self.view addSubview:_selectedDate];
//    _selectedDate.text=[df stringFromDate:datepicker.date];
//}

//- (void)LabelChange:(id)sender{
//    NSDateFormatter *df = [[NSDateFormatter alloc] init];
//    [df setDateFormat:@"MM-dd-yyyy HH:mm:ss"];
//    _selectedDate.text = [NSString stringWithFormat:@"%@",
//                          [df stringFromDate:datepicker.date]];
//    [self getTimeDifferenceToFire];
//    
//        // [datepicker removeFromSuperview];
//}

//-(void)getTimeDifferenceToFire{
//    NSDate *today = [NSDate date];
//    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
//        // display in 12HR/24HR (i.e. 11:25PM or 23:25) format according to User Settings
//    [dateFormatter setDateFormat:@"MM-dd-yyyy HH:mm:ss"];
//    NSString *currentTime = [dateFormatter stringFromDate:today];
//    
//    NSLog(@"User's current time in their preference format:%@",currentTime);
//    NSDate* date2 = datepicker.date;
//    NSTimeInterval distanceBetweenDates = [date2 timeIntervalSinceDate:today];
//    double secondsInAnHour = 3600;
//    NSInteger hoursBetweenDates = distanceBetweenDates / secondsInAnHour;
//    
//    NSLog(@"=========== story will get posted in %ldhors and %f seconds",(long)hoursBetweenDates,secondsInAnHour);
//    
//    
////    [self setupLocalNotifications:secondsInAnHour];
//    
//    
//}

//
//-(void)alertPosted:(UIImage *)imageName withMessage:(NSString *)msg{
//    
//    NSIndexPath *path=[SingletonClass sharedState].selectedUserIndex;
//    FBSDKAccessToken *token = [SUCache itemForSlot:path.section+path.row].token;
//    [FBSDKAccessToken setCurrentAccessToken:token];
//    NSLog(@"FBID %@",[FBSDKAccessToken currentAccessToken].userID);
//    
//    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Post scheduled successfully"
//                                                    message:@""
//                                                   delegate:self
//                                          cancelButtonTitle:@"OK"
//                                          otherButtonTitles:nil];
//        //[alert show];
//    
//    UIImage *image = [UIImage imageNamed:@"hel.jpeg"];
//    NSString *str= @"hello";
//    
//    NSLog(@"image- %@ and strs- %@",image,str);
//    self.imageToPost=nil;
//    self.stringToPost = nil;
//    
//        // [self setupLocalNotifications:1];
//}


//- (void) SaveImagesToSql: (NSData*) imgData withUrl:(NSString*)mainUrl withdate:(NSString *)myDate
//{
//    NSString *docsDir;
//    NSArray *dirPaths;
//    
//    NSLog(@"intvalue %ld",(long)[myDate integerValue] );
//    
//    dirPaths=NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//    NSLog(@"dirPath in load: %@",dirPaths);
//    
//    
//    docsDir=[dirPaths objectAtIndex:0];
//    NSLog(@"docsDir in load: %@",docsDir);
//    databasePath =[[NSString alloc] initWithString:[docsDir stringByAppendingPathComponent:@"contacts.db"]];
//    
//    [self setupLocalNotifications:myDate];
//    NSDate *mydate  = [NSDate date];
//    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
//    
//    
//    NSString  *dateString;
//    [formatter setDateFormat:@"dd-MM-yyyy HH:mm:ss"];
//    
//    dateString = [formatter stringFromDate:[NSDate date]];
//    
//    
//    sqlite3_stmt *statement;
//    const char *dbpath=[databasePath UTF8String];
//    
//    
//    if(sqlite3_open(dbpath, &contactDB)==SQLITE_OK)
//    {
//            //        NSData *imgdata=UIImagePNGRepresentation(self.images);
//        
//        
//        
//        const char *sql="insert into IMAGES(Url,IMAGE,CDATE) values(?,?,?)";
//            //const char *string=[str UTF8String];
//        if(sqlite3_prepare_v2(contactDB, sql, -1,&statement, NULL)==SQLITE_OK){
//            sqlite3_bind_text(statement, 1, [mainUrl UTF8String], -1, NULL);
//            
////         sqlite3_bind_blob(statement, 2, [imgData bytes], [imgData length], NULL);
//         sqlite3_bind_text(statement, 3, [myDate UTF8String], -1, NULL);
//            
//            NSLog(@"details saved");
//                //            NSLog(@"saved succussfully");
//        }
//        if (sqlite3_step(statement)==SQLITE_DONE) {
//                //            NSLog(@"details saved");
//            NSLog(@"saved succussfully");
//        }
//            //        sqlite3_step(statement);
//            //status.text=@"saved succussfully";
//    }
//}

-(void)viewWillAppear:(BOOL)animated{
    self.navigationController.navigationBarHidden=YES;
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    self.mediaCheck = YES;
    UIImage *image = [info objectForKey:UIImagePickerControllerEditedImage];
    if(image)
    {
        self.postImg.image = image;
        self.imageToPost = image;
//        [self convertImageToData:image];
        [self dismissViewControllerAnimated:YES completion:^{
            PostScheduleController *postSchedule=[[PostScheduleController alloc] init];
         postSchedule.type=@"Video";
             postSchedule.title=@"Schedule Post";
            postSchedule.selectedImage=image;
            [self.navigationController pushViewController:postSchedule animated:YES];
            
                  }];
    }
    
}

//-(void)convertImageToData:(UIImage *)image1{
//        //get user id.
//    
//    
//    NSString *userId = @"265952946944590";
//    
//    
//        //convert image to data
//    UIImage *saveImage =image1;
//    NSData *imgData= UIImageJPEGRepresentation(saveImage,0.0);
//    
//        //send date data
//    time_t unixTime = (time_t) [[NSDate date] timeIntervalSince1970];
////    NSNumber * numbToStore=[NSNumber numberWithInteger:unixTime];
//    NSString *str= [NSString stringWithFormat:@"%ld",unixTime];
//        //call method to save in database
//    
//    [self SaveImagesToSql:imgData withUrl:userId withdate:str];
//}


- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [self dismissViewControllerAnimated:YES completion:nil];
}


//-(void)alertPosted:(id)sender{
//    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Post scheduled successfully"
//                                                    message:@""
//                                                   delegate:self
//                                          cancelButtonTitle:@"OK"
//                                          otherButtonTitles:nil];
//    [alert show];
//}

-(BOOL)pickFromLibrary:(id)sender{
    
    if (([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary] == NO
         && [UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum] == NO)) {
        return NO;
    }
    
    UIImagePickerController *cameraUI = [[UIImagePickerController alloc] init];
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary]
        && [[UIImagePickerController availableMediaTypesForSourceType:UIImagePickerControllerSourceTypePhotoLibrary] containsObject:(NSString *)kUTTypeImage]) {
        
        cameraUI.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        cameraUI.mediaTypes = [NSArray arrayWithObject:(NSString *) kUTTypeImage];
        
    } else if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum]
               && [[UIImagePickerController availableMediaTypesForSourceType:UIImagePickerControllerSourceTypeSavedPhotosAlbum] containsObject:(NSString *)kUTTypeImage]) {
        
        cameraUI.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum;
        cameraUI.mediaTypes = [NSArray arrayWithObject:(NSString *) kUTTypeImage];
        
    } else {
        return NO;
    }
    
    cameraUI.allowsEditing = YES;
    cameraUI.delegate = self;
    
    [self presentViewController:cameraUI animated:YES completion:nil];
    return YES;
    
}





-(void)shareWithoutDialog{
    
//    NSString *facebookStatusMessage = @"facebookStatusMessage";
//    NSMutableDictionary* params = [NSMutableDictionary dictionaryWithObjectsAndKeys:
//                          facebookStatusMessage, @"status",
//nil];
    
    
//   FBRequest *req = [FBRequest requestWithGraphPath:@"me/feed" parameters:params HTTPMethod:@"POST"];
   
}

//-(void) storyPostwithDictionary:(NSDictionary *)dict{
//    
//    NSString *title = [NSString stringWithFormat:@"New highscore"];
//    NSString *description = [NSString stringWithFormat:@"I got new highscore %@ in level %@. CaveRunMowgli is an adventurous game which takes you on a voyage with Mowgli.Play multiple levels of fun packed adventurous routes of Mowgli.",@"100",@"1"];
//    
//    NSDictionary *dict1 = [NSDictionary dictionaryWithObjectsAndKeys:@"highscore",FacebookType,title,FacebookTitle,description,FacebookDescription,@"get",FacebookActionType, nil];
//
//    
//    NSString *type = [NSString stringWithFormat:@"%@",[dict1 objectForKey:@"OpenStoryFacebookType"]];
//    
//   NSString *actionType = [NSString stringWithFormat:@"/me/sdphantom:%@",[dict1 objectForKey:FacebookActionType]];
//    NSLog(@"Type = %@", type);
//    NSLog(@"Action  =%@",actionType);
//    
//    
//    //fbstaging://graph.facebook.com/staging_resources/MDExNDQyNjIzMzQyNjc3NjM2OjUxMDY4MTkwNg==
//    
//    id<FBGraphObject> object =
//    [FBGraphObject openGraphObjectForPostWithType:[NSString stringWithFormat:@"sdphantom:%@",type] title:title image:@"fbstaging://graph.facebook.com/staging_resources/MDExNDUzNzA0OTM0OTAyODEwOjQxMjYzOTIzOA==" url:@"https://itunes.apple.com/app/id903882797" description:description];
//
//    
//    id<FBOpenGraphAction> action = (id<FBOpenGraphAction>)[FBGraphObject graphObject];
//    [action setObject:object forKey:type];
//    
//    // create action referencing user owned object
//    [FBRequestConnection startForPostWithGraphPath:actionType graphObject:action completionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
//        if(!error) {
//            NSLog(@"OG story posted, story id: %@", [result objectForKey:@"id"]);
//            //            [[[UIAlertView alloc] initWithTitle:@"OG story posted" message:@"Check your Facebook profile or activity log to see the story."
//            //                                       delegate:self
//            //                              cancelButtonTitle:@"OK!"
//            //                              otherButtonTitles:nil] show];
//        } else {
//            // An error occurred
//            NSLog(@"Encountered an error posting to Open Graph: %@", error);
//        }
//    }];
//}


#pragma mark TextFieldDelegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    return YES;
}// return NO to disallow editing.

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    
//    [self fetchFeeds:@"nil"];
    // [feedTable reloadData];
    return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField{
    [textField becomeFirstResponder];
    //if textField is stateTextField return statePicker
    
}

- (BOOL)textFieldShouldEndEditing:(UITextField *)textField{
    //    self.datePicker.hidden=YES;
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField{
    // int y=0;
}

//-(void) shareOnFacebookFeedDialog:(NSDictionary *)params{
//    
//     NSString *str = [NSString stringWithFormat:@"I beat you in Level  with  score   . CaveRunMowgli is an adventurous game which takes you on a voyage with Mowgli.Play multiple levels of fun packed adventurous routes of Mowgli"];
//    
//    NSMutableDictionary *params1 =
//    [NSMutableDictionary dictionaryWithObjectsAndKeys:
//     str, @"description", @"", @"caption",
//     @"https://itunes.apple.com/app/id903886678", @"link",@"Cave Run Mowgli",@"name",
//     @"i.imgur.com/1612f7A.png?1",@"picture"
//    , nil];
//    
//    [FBWebDialogs presentFeedDialogModallyWithSession:nil parameters:params1 handler:^(FBWebDialogResult result, NSURL *resultUrl, NSError *error){
//        
//        if (error) {
//            NSLog(@"Error to post on facebook = %@", [error localizedDescription]);
//        }
//        else{
//            
//            if (result == FBWebDialogResultDialogNotCompleted) {
//                NSLog(@"Error to post on facebook = %@", [error localizedDescription]);
//                NSLog(@"User cancel Request");
//            }//End Result Check
//            else{
//                NSString *sss= [NSString stringWithFormat:@"%@",resultUrl];
//                if ([sss rangeOfString:@"post_id"].location == NSNotFound) {
//                    NSLog(@"User Cancel Share");
//                }
//                else{
//                    NSLog(@"posted on wall");
//                    
//                    
//                }
//            }//End Else Block Result Check
//        }
//    }];
//    
//}


//- (NSURLSession *)backgroundSession
//{
//    /*
//     Using disptach_once here ensures that multiple background sessions with the same identifier are not created in this instance of the application. If you want to support multiple background sessions within a single process, you should create each session with its own identifier.
//     */
//    static NSURLSession *session = nil;
//    static dispatch_once_t onceToken;
//    dispatch_once(&onceToken, ^{
////        NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration backgroundSessionConfiguration:@"com.example.apple-samplecode.SimpleBackgroundTransfer.BackgroundSession"];
////        session = [NSURLSession sessionWithConfiguration:configuration delegate:self delegateQueue:nil];
//    });
//    
//    NSLog(@"welcome in background session.....");
//    
//    //  /*  How to automatic call function every x time(check alert method name....
//    //
//    //[NSTimer scheduledTimerWithTimeInterval:20.0 target:self
//                                  // selector:@selector(backgroundSession) userInfo:nil repeats:NO];
//    [self shareOnFacebookFeedDialog:nil];
//    
//    return session;
//}


@end
