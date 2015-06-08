//
//  PostScheduleController.m
//  SwitchUserSample
//

//
//

#import "PostScheduleController.h"
#import <MobileCoreServices/MobileCoreServices.h>
#import <CoreLocation/CoreLocation.h>
#import <FacebookSDK/FacebookSDK.h>
#import <sqlite3.h>
#import "SUCache.h"
#import "ScheduledPostController.h"
#import "SingletonClass.h"

@interface PostScheduleController ()

@end

@implementation PostScheduleController

- (void)viewDidLoad {
    [super viewDidLoad];
     self.view.backgroundColor = [UIColor colorWithRed:250.0f/255.0f green:250.0f/255.0f blue:250.0f/255.0f alpha:.6f];
    self.scrollView=[[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
    self.scrollView.backgroundColor=[UIColor clearColor];
    self.scrollView.delegate=self;
    self.scrollView.contentSize=CGSizeMake(self.view.frame.size.width,self.view.frame.size.height+300);
    [self.view addSubview:self.scrollView];
    
    
    self.selectedImageView=[[UIImageView alloc] initWithFrame:CGRectMake(150,30, 130, 110)];
    self.selectedImageView.hidden=NO;
    self.selectedImageView.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"upload_image.png"]];
       self.selectedImageView.userInteractionEnabled=YES;
    
       self.selectedImageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.selectedImageView.layer.shadowOpacity = 0.6f;
    self.selectedImageView.layer.shadowOffset = CGSizeMake(0.0f, 5.0f);
    self.selectedImageView.layer.shadowRadius = 10.0f;
    self.selectedImageView.layer.masksToBounds = NO;
    [self.scrollView addSubview:self.selectedImageView];
    UIGestureRecognizer *gesture=[[UIGestureRecognizer alloc] initWithTarget:self action:@selector(pickFromLibrary:)];
    [self.selectedImageView addGestureRecognizer:gesture];
    [self.scrollView bringSubviewToFront:self.selectedImageView];

    self.profileImageView=[[FBSDKProfilePictureView alloc] initWithFrame:CGRectMake(20, 30, 110, 110)];
    self.profileImageView.layer.borderColor=[[UIColor blackColor] CGColor];
    self.profileImageView.layer.borderWidth=.5f;
    self.profileImageView.backgroundColor = [UIColor whiteColor];
    self.profileImageView.layer.shadowColor = [UIColor blackColor].CGColor;
    self.profileImageView.layer.shadowOpacity = 0.6f;
    self.profileImageView.layer.shadowOffset = CGSizeMake(0.0f, 5.0f);
    self.profileImageView.layer.shadowRadius = 10.0f;
    self.profileImageView.layer.masksToBounds = NO;
    [self.scrollView addSubview:self.profileImageView];
    
     self.pageTextField = [[UITextView alloc] init];
     self.pageTextField.font = [UIFont systemFontOfSize:15];
//     self.pageTextField.placeholder  = @" Enter Your text to post";
     self.pageTextField.autocorrectionType = UITextAutocorrectionTypeNo;
     self.pageTextField.keyboardType = UIKeyboardTypeDefault;
     self.pageTextField.returnKeyType = UIReturnKeyDefault;
     self.pageTextField.delegate = self;
    self.pageTextField.backgroundColor=[UIColor whiteColor];
    self.pageTextField.frame = CGRectMake(10,155, self.view.frame.size.width-20, 130);
    self.pageTextField.layer.borderColor=[[UIColor blackColor] CGColor];
    self.pageTextField.layer.borderWidth=.5f;
    self.pageTextField.backgroundColor = [UIColor whiteColor];
    self.pageTextField.layer.shadowColor = [UIColor blackColor].CGColor;
    self.pageTextField.layer.shadowOpacity = 0.6f;
    self.pageTextField.layer.shadowOffset = CGSizeMake(0.0f, 5.0f);
    self.pageTextField.layer.shadowRadius = 10.0f;
    self.pageTextField.layer.masksToBounds = NO;
     [self.scrollView addSubview:self.pageTextField];
    self.pageTextField.text=@"Write Something..";
    self.view.backgroundColor=[UIColor whiteColor];
    
    
    UIBarButtonItem *item1=[[UIBarButtonItem alloc] initWithTitle:@"Schedule" style:UIBarButtonItemStylePlain target:self action:@selector(postContent)];
    [item1 setTintColor:[UIColor blueColor]];
   self.navigationItem.rightBarButtonItem=item1;
    self.navigationController.navigationItem.rightBarButtonItem=item1;
    
    UIBarButtonItem *item2=[[UIBarButtonItem alloc] initWithTitle:@"Back" style:UIBarButtonItemStylePlain target:self action:@selector(GoBack)];
//    self.navigationItem.leftBarButtonItem=item2;

    [item2 setTintColor:[UIColor blueColor]];
//    [self.navigationController.navigationBar.n addSubview:item2];
    self.navigationController.navigationItem.leftBarButtonItem=item2;

    toolBar = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, 320, 56)];
    [toolBar setBarStyle:UIBarStyleDefault];
    
    NSMutableArray *barItems = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *Photo = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"camera.png"] style:UIBarButtonItemStylePlain target:self action:@selector(addPhotos)];
    
    [barItems addObject:Photo];
    
    
    UIBarButtonItem *space=[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:self action:@selector(ShowClock)];
    [barItems addObject:space];
    
    

    
    UIBarButtonItem *clockSchedule = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"schedule.png"] style:UIBarButtonItemStylePlain target:self action:@selector(ShowClock)];
    
    [barItems addObject:clockSchedule];
    
//    UIBarButtonItem *location = [[UIBarButtonItem alloc]  initWithImage:[UIImage imageNamed:@"place.png"] style:UIBarButtonItemStylePlain target:self action:@selector(addLocation)];
//    
//    [barItems addObject:location];
//    
//    UIBarButtonItem *emotions = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"smiley.png"]  style:UIBarButtonItemStylePlain target:self action:@selector(addEmotions)];
//    
//    [barItems addObject:emotions];

        [toolBar setItems:barItems animated:YES];
    
    
//    self.pageTextField.inputAccessoryView = toolBar;

}

-(void)doneButtonItem{
    pickerToolBar.hidden=YES;
    datepicker.hidden=YES;
    pickerView.hidden=YES;
}

-(void)textFieldDidBeginEditing:(UITextField *)textField{
//    [textField becomeFirstResponder];
}

-(void)textFieldDidEndEditing:(UITextField *)textField{
    

}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
  
    UIImage *image = [info objectForKey:UIImagePickerControllerEditedImage];
    if(image)
    {
        self.selectedImageView.image=image;
    }
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(BOOL)pickFromLibrary:(id)sender{
    [self.pageTextField resignFirstResponder];
    self.selectedImageView.hidden=NO;
    self.selectedImageView.layer.borderColor=[[UIColor blackColor] CGColor];
    self.selectedImageView.layer.borderWidth=.5f;

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

//    ScheduledPostController *schedule=[[ScheduledPostController alloc] init];
//    schedule.title=@"Scheduled";
//    [self.navigationController pushViewController:schedule animated:YES];
    return YES;
    
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [self dismissViewControllerAnimated:YES completion:nil];
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{

    [textField resignFirstResponder];
//    if ([self.type isEqualToString:@"Text"]) {
//        toolBar.frame=CGRectMake(0, self.view.frame.size.height-50, self.view.frame.size.width, 50);
//        [self.view addSubview:toolBar];
////        [self.view addSubview:toolBar];
//    }
    return YES;
}

-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    if ([self.type isEqualToString:@"Text"]) {
    
    }
    return YES;
}
-(void)addLocation{
   
}
-(void)addEmotions{

}
-(void)addPhotos{
    datepicker.hidden=YES;
    [self pickFromLibrary:self];
}
-(void)ShowClock{
    if (!pickerView) {
        
       pickerView =[[UIView alloc] initWithFrame:CGRectMake(0, 250, 325, 300)];
        pickerView.backgroundColor=[UIColor clearColor];
        [self.view addSubview:pickerView];
        
        pickerToolBar = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, 320, 56)];
        [pickerToolBar setBarStyle:UIBarStyleDefault];
        NSMutableArray *pickerbarItems = [[NSMutableArray alloc] init];
        datepicker=[[UIDatePicker alloc] initWithFrame:CGRectMake(0, 57, 325, 300)];
        datepicker.datePickerMode = UIDatePickerModeDateAndTime;
        datepicker.hidden = NO;
        datepicker.date = [NSDate date];
        datepicker.backgroundColor=[UIColor whiteColor];
        datepicker.tintColor=[UIColor blueColor];
        [datepicker addTarget:self
                       action:@selector(LabelChange:)
             forControlEvents:UIControlEventValueChanged];
        [pickerView addSubview:datepicker];
        
        UIBarButtonItem *doneButtItem=[[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStylePlain target:self action:@selector(doneButtonItem)];
        [pickerbarItems addObject:doneButtItem];
        [pickerToolBar setItems:pickerbarItems animated:YES];
        [pickerView addSubview:pickerToolBar];
        
    }else{
        datepicker.hidden=NO;
        pickerToolBar.hidden=NO;
        pickerView.hidden=NO;
    }
     //this can set value of selected date to your label change according to your condition
    
    
    NSDateFormatter * df = [[NSDateFormatter alloc] init];
    [df setDateFormat:@"MM-dd-yyyy HH:mm:ss"]; // from here u can change format..
    
//    _selectedDate = [[UILabel alloc]init];
//    _selectedDate.frame=CGRectMake(100, 150, 250, 50);
//        //[self.view addSubview:_selectedDate];
//    _selectedDate.text=[df stringFromDate:datepicker.date];

}

- (void)LabelChange:(id)sender{
    
//    datepicker.hidden=YES;
//    pickerToolBar.hidden=YES;
}

-(void)GoBack{
    [self.navigationController popViewControllerAnimated:YES];
}
-(void)viewDidDisappear:(BOOL)animated{
    self.navigationController.navigationBarHidden=YES;
}

-(void)viewWillAppear:(BOOL)animated{
    self.navigationController.navigationBarHidden=NO;
    NSLog(@"[FBSDKAccessToken currentAccessToken].userID  %@",[FBSDKAccessToken currentAccessToken].userID);
    
    NSIndexPath  *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
    
    }

    self.profileImageView.profileID=[FBSDKAccessToken currentAccessToken].userID;
    if ([self.type isEqualToString:@"Text"]) {
//        [self.pageTextField becomeFirstResponder];
        
    }else{
//        self.pageTextField.placeholder=@"Write Something...";
         self.selectedImageView.hidden=NO;
        self.selectedImageView.image=self.selectedImage;
       
    }
    toolBar.frame=CGRectMake(0, self.view.frame.size.height-50, self.view.frame.size.width, 50);
    self.pageTextField.text=@"Write Something..";
    [self.view addSubview:toolBar];
//   self.pageTextField.inputAccessoryView=toolBar;
}


-(void)textViewDidEndEditing:(UITextView *)textView{


}

- (BOOL)textViewShouldBeginEditing:(UITextView *)textView;{

    if ([textView.text isEqualToString:@"Write Something.."]) {
        textView.text=nil;
    }
    return YES;
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text{

    
    if ([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
    }
    return YES;
}
-(void)alertPosted:(UIImage *)imageName withMessage:(NSString *)msg{
    
        //    NSIndexPath *path=[SingletonClass sharedState].selectedUserIndex;
        //    FBSDKAccessToken *token = [SUCache itemForSlot:path.section+path.row].token;
        //    [FBSDKAccessToken setCurrentAccessToken:token];
        //    NSLog(@"FBID %@",[FBSDKAccessToken currentAccessToken].userID);
      [self getTimeDifferenceToFire];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Post scheduled successfully"
                                                    message:@""
                                                   delegate:self
                                          cancelButtonTitle:@"OK"
                                          otherButtonTitles:nil];
    [alert show];
    
  
    
}

-(void)getTimeDifferenceToFire{
    NSDate *today = [NSDate date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        // display in 12HR/24HR (i.e. 11:25PM or 23:25) format according to User Settings
    [dateFormatter setDateFormat:@"MM-dd-yyyy HH:mm:ss"];
    NSString *currentTime = [dateFormatter stringFromDate:today];
    
    NSLog(@"User's current time in their preference format:%@",currentTime);
    NSDate* date2 = datepicker.date;
    NSTimeInterval distanceBetweenDates = [date2 timeIntervalSinceDate:today];
    double secondsInAnHour = 3600;
    NSInteger hoursBetweenDates = distanceBetweenDates / secondsInAnHour;
    
    NSLog(@"=========== story will get posted in %ldhors and %f seconds",(long)hoursBetweenDates,distanceBetweenDates);
    
    
    [self setupLocalNotifications:distanceBetweenDates];
    
}


- (void)setupLocalNotifications:(double )seconds {
    
    NSIndexPath *path=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:path.section+path.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
    NSString *userId = [FBSDKAccessToken currentAccessToken].userID;
  
        //NSString *userId = @"265952946944590";
    
    
    self.localNotification = [[UILocalNotification alloc] init];
    if (![self.pageTextField.text isEqualToString:@""]) {
        self.localNotification.alertBody = self.pageTextField.text;
    }else{
        self.localNotification.alertBody = @"Reminder to post";
    }
    
    
    NSMutableDictionary * dictMessage=[[NSMutableDictionary alloc]init];
    [dictMessage setObject:self.localNotification.alertBody forKey:@"Text"];
    
    NSDate *now = [NSDate date];
    dateToFire = [now dateByAddingTimeInterval:seconds];
    NSLog(@"fire time: %@", dateToFire);
    self.localNotification.fireDate = dateToFire;
    self.localNotification.soundName = UILocalNotificationDefaultSoundName;

    time_t unixTime = (time_t) [dateToFire timeIntervalSince1970];
    self.timeStamp = [NSString stringWithFormat:@"%ld",unixTime];
    
    [dictMessage setObject:self.timeStamp forKey:@"TimeStamp"];
    [dictMessage setObject:userId forKey:@"UserId"];
    if (self.selectedImage || self.selectedImageView.image) {
        [dictMessage setObject:@"true" forKey:@"image"];
    }else{
        [dictMessage setObject:@"false" forKey:@"image"];
    }
    
    
    NSLog(@"notification body %@ and time stamp %@ and userId %@",self.localNotification.alertBody,_timeStamp,userId);
    [self.localNotification setUserInfo:dictMessage];
    
  [[UIApplication sharedApplication] scheduleLocalNotification: self.localNotification];
    
   [self convertImageToData:self.selectedImageView.image];
    
}


#pragma mark - save images to sqlite
#pragma mark------------------

-(void)convertImageToData:(UIImage *)image1{
        //get user id.
    
    NSIndexPath *path=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:path.section+path.row].token;
    [FBSDKAccessToken setCurrentAccessToken:token];
    NSString *userId = [FBSDKAccessToken currentAccessToken].userID;
    

    
        //NSString *userId = @"265952946944590";
    
    
        //convert image to data
    UIImage *saveImage =image1;
    NSData *imgData= UIImageJPEGRepresentation(saveImage,0.0);
    
        //send date data
    time_t unixTime = (time_t) [dateToFire timeIntervalSince1970];
    self.timeStamp = [NSString stringWithFormat:@"%ld",unixTime];
    
        //    NSNumber * numbToStore=[NSNumber numberWithLong:unixTime];
    
        //call method to save in database
    
    [self SaveImagesToSql:imgData withUrl:userId withdate:self.timeStamp];
}


    // Save Small Image Data by given main url

- (void) SaveImagesToSql: (NSData*) imgData withUrl:(NSString*)mainUrl withdate:(NSString *)myDate
{
    NSLog( @"\n*****Save image to SQLite*****\n" );
    
    
//    NSString *tokenString= [FBSDKAccessToken currentAccessToken].tokenString ;
    
    const char* sqliteQuery = "INSERT INTO IMAGES (URL, IMAGE, CDATE,ATOKEN) VALUES (?,?,?,?)";
    sqlite3_stmt* statement;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    
    NSLog(@"%@",paths);
    NSString *documentsDirectory = [paths objectAtIndex:0] ;
    NSString *databasePath = [documentsDirectory stringByAppendingPathComponent:@"ImageDataBase.sqlite"];
    
    if(sqlite3_open([databasePath UTF8String], &_databaseHandle)==SQLITE_OK){
        
        if( sqlite3_prepare_v2(_databaseHandle, sqliteQuery, -1, &statement, NULL) == SQLITE_OK )
        {
            sqlite3_bind_text(statement, 1, [mainUrl UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_blob(statement, 2, [imgData bytes],(int)[imgData length] , SQLITE_TRANSIENT);
            sqlite3_bind_text(statement, 3, [myDate UTF8String], -1, SQLITE_TRANSIENT);
            sqlite3_bind_text(statement, 4, [self.pageTextField.text UTF8String] , -1, SQLITE_TRANSIENT);
            
            if ( sqlite3_step(statement)==SQLITE_DONE) {
                NSLog(@"successfully saved data to database");
                
                UIAlertView *alert=[[UIAlertView alloc]initWithTitle:@"Post successfuly scheduled" message:nil delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
                [alert show];
            }
            
        }
        else NSLog( @"SaveBody: Failed from sqlite3_prepare_v2. Error is:  %s", sqlite3_errmsg(_databaseHandle) );
            // Finalize and close database.
        sqlite3_finalize(statement);
        sqlite3_close(_databaseHandle);
        
//        self.imageToPost=nil;
//        self.stringToPost = nil;
        self.pageTextField.text=nil;
        self.selectedImageView.image=nil;
        self.selectedImage=nil;
        
    }
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)postContent{

//    if (self.selectedImageView.image&&self.pageTextField.text) {
//         [self alertPosted:self.selectedImageView.image withMessage:self.pageTextField.text];
//    }
//      else  if (self.selectedImage&&self.pageTextField.text) {
//         [self alertPosted:self.selectedImage withMessage:self.pageTextField.text];
//    }else if  (self.selectedImage) {
//        [self alertPosted:self.selectedImage withMessage:nil];
//
//    }else if (self.pageTextField.text){
//        [self alertPosted:nil withMessage:self.pageTextField.text];
//
//    }
    [self getTimeDifferenceToFire];
   
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
@end
