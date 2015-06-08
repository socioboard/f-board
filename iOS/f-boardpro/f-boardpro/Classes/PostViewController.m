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
#import "ScheduledPostController.h"
#import "TableCustomCell.h"
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

    self.view.backgroundColor = [UIColor colorWithRed:250.0f/255.0f green:250.0f/255.0f blue:250.0f/255.0f alpha:.8f];
   

    UIView *labelView=[[UIView alloc] initWithFrame:CGRectMake(5, 10, self.view.frame.size.width-10, 50)];
    labelView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:labelView];
    
    self.postImgButton =  [UIButton buttonWithType:UIButtonTypeCustom];
    self.postImgButton.frame = CGRectMake(labelView.frame.size.width+2-50,5, 50, 50);
    [self.postImgButton setImage:[UIImage imageNamed:@"newpost@3x.png"]  forState:UIControlStateNormal] ;
    [self.postImgButton addTarget:self
                           action:@selector(pickFromLibrary:)
                 forControlEvents:UIControlEventTouchUpInside];
    [labelView addSubview:self.postImgButton];

    
    self.textPostButton =  [UIButton buttonWithType:UIButtonTypeCustom];
    self.textPostButton.frame = CGRectMake(0, 10, 200, 30);
    self.textPostButton.backgroundColor=[UIColor whiteColor];
    [self.textPostButton setTitle:@"Scheduled posts : 0"   forState:UIControlStateNormal] ;
    [self.textPostButton addTarget:self
                           action:@selector(textPostButtonClick:)
                 forControlEvents:UIControlEventTouchUpInside];
    [self.textPostButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [self.textPostButton.titleLabel setFont:[UIFont fontWithName:@"" size:2.0f]];
    [labelView addSubview:self.textPostButton];
    [self createFollowTable];
    }

-(void)retriveAllSchedulePostCount{
    
    NSString* sqliteQuery = [NSString stringWithFormat:@"SELECT * FROM IMAGES"];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSLog(@"-------%@",paths);
//    sqlite3_stmt *stmt=nil;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    databasePath = [documentsDirectory stringByAppendingPathComponent:@"ImageDataBase.sqlite"];
    
    sqlite3_stmt* statement;
    if(sqlite3_open([databasePath UTF8String], &_databaseHandle)==SQLITE_OK){
        if( sqlite3_prepare_v2(_databaseHandle, [sqliteQuery UTF8String], -1, &statement, NULL) == SQLITE_OK )
        {
            while( sqlite3_step(statement) == SQLITE_ROW )
            {
                 int length = sqlite3_column_int(statement, 0);
                NSString *string1=[NSString stringWithFormat:@"Scheduled posts : %d",length];
                  [self.textPostButton setTitle:string1   forState:UIControlStateNormal] ;
//                NSString *strURl = [NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 0)];
               
                NSLog(@"name string %@",string1);
                
            }
        }else{
            NSLog( @"SaveBody: Failed from sqlite3_prepare_v2. Error is:  %s", sqlite3_errmsg(_databaseHandle) );
        }
    }
        // Finalize and close database.
    sqlite3_finalize(statement);
    sqlite3_close(_databaseHandle);

    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    NSString *strMsg=[self.TextArray objectAtIndex:indexPath.row];
    
    
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:strMsg];
        // Add Background Color for Smooth rendering
    [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
        // Add Main Font Color
    [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
        // Add paragraph style
    NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
    [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
    [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
        // Add Font
    [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
        // And finally set the text on the label to use this
        //    label.attributedText = attributedString;
    
        // Phew. Now let's make the Bounding Rect
    CGRect boundingRect = [attributedString boundingRectWithSize:CGSizeMake(tableView.frame.size.width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
    
        // Now let's set the label size from there
        //    CGFloat frame = CGRectMake(label.frame.origin.x, label.frame.origin.y, boundingRect.size.width, boundingRect.size.height);
    
    return 60 +boundingRect.size.height;
}



-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return self.fbIDArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Follow";
    
    TableCustomCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    NSString *fromIdImage = [self.fbIDArray objectAtIndex:indexPath.row];
    
    if (![fromIdImage isEqualToString:@"nil"]) {
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:fromIdImage] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    cell.fromUserImage.frame=CGRectMake(10, 22, 40, 40);
    cell.userNameDesc.text=[self.TextArray objectAtIndex:indexPath.row];
    CGSize maximumLabelSize = CGSizeMake(296, FLT_MAX);
    CGSize expectedLabelSize = [cell.userNameDesc.text sizeWithFont:[UIFont fontWithName:@"ariel" size:5] constrainedToSize:maximumLabelSize lineBreakMode: NSLineBreakByWordWrapping];
        //adjust the label the the new height.
    CGRect newFrame = cell.userNameDesc.frame;
    newFrame.size.height = expectedLabelSize.height;
    newFrame.size.width = cell.userNameDesc.frame.size.width;
        //    cell.userNameDesc.frame = newFrame;
    cell.userNameDesc.frame=CGRectMake(65, 12,tableView.frame.size.width-115, newFrame.size.height);
    [cell.userNameDesc sizeToFit];
    cell.likeCountLabel.text = [self.TimeArray objectAtIndex:indexPath.row];
   

    cell.commentCountLabel.text = @"";
    UIImage *Postedimage = [UIImage imageWithData:[self.dataArray objectAtIndex:indexPath.row]];
    
    cell.userImage.image=Postedimage;
    cell.userImage.frame=CGRectMake(tableView.frame.size.width-60, 15, 50, 50);
     cell.likeCountLabel.frame=CGRectMake(65, cell.userNameDesc.frame.size.height+15,tableView.frame.size.width-115, 10);
    [cell.likeCountLabel setTextColor:[UIColor grayColor]];
    return cell;
}


-(void)retriveAllSchedulePost{
    self.TextArray=[[NSMutableArray alloc] init];
    self.dataArray=[[NSMutableArray alloc] init];
    self.fbIDArray=[[NSMutableArray alloc] init];
    self.TimeArray=[[NSMutableArray alloc] init];
    NSData* data = nil;
    NSString* sqliteQuery = [NSString stringWithFormat:@"SELECT * FROM IMAGES"];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSLog(@"-------%@",paths);
     NSString *documentsDirectory = [paths objectAtIndex:0];
     databasePath = [documentsDirectory stringByAppendingPathComponent:@"ImageDataBase.sqlite"];
    
    sqlite3_stmt* statement;
    if(sqlite3_open([databasePath UTF8String], &_databaseHandle)==SQLITE_OK){
        if( sqlite3_prepare_v2(_databaseHandle, [sqliteQuery UTF8String], -1, &statement, NULL) == SQLITE_OK )
        {
            while( sqlite3_step(statement) == SQLITE_ROW )
            {
                
                NSString *strURl = [NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 0)];
                [self.fbIDArray addObject:strURl];
                int length = sqlite3_column_bytes(statement, 1);
                data       = [NSData dataWithBytes:sqlite3_column_blob(statement, 1) length:length];
                [self.dataArray addObject:data];
                self.accesstokenString = [NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 2)];
                NSLog(@"access Token string %@",self.accesstokenString);
                NSString *strText=[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 3)];
                [self.TextArray addObject:strText];
                
                NSLog(@"name string %f",[self.accesstokenString doubleValue]);
                NSDate *date = [NSDate dateWithTimeIntervalSince1970:[self.accesstokenString doubleValue]];
                NSDateFormatter *formatter= [[NSDateFormatter alloc] init];
                [formatter setDateFormat:@"dd/MMM/yyyy hh:mm:ss a"];
               
              [self.TimeArray addObject:[formatter stringFromDate:date]];
            }
        }else{
            NSLog( @"SaveBody: Failed from sqlite3_prepare_v2. Error is:  %s", sqlite3_errmsg(_databaseHandle) );
        }
    }
        // Finalize and close database.
    sqlite3_finalize(statement);
    sqlite3_close(_databaseHandle);
    
    NSString *string1=[NSString stringWithFormat:@"Scheduled posts : %lu",(unsigned long)self.TextArray.count];
    [self.textPostButton setTitle:string1   forState:UIControlStateNormal] ;

    [self.groupTableView reloadData];
    
    
}



-(void)VideoPostButton:(UIButton *)button{

}


-(void)textPostButtonClick:(UIButton *)button{
//    ScheduledPostController *schedule=[[ScheduledPostController alloc] init];
//    schedule.title=@"Scheduled";
//    [self.navigationController pushViewController:schedule animated:YES];
}



-(void)eventPostButton:(UIButton *)button{

}
-(void)createFollowTable
{
    self.groupTableView=[[UITableView alloc]initWithFrame:CGRectMake(5, 70, self.view.frame.size.width-10,self.view.frame.size.height-130) style:UITableViewStylePlain];
    self.groupTableView.dataSource=self;
    self.groupTableView.delegate=self;
    self.groupTableView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:self.groupTableView];
}


-(void)viewWillAppear:(BOOL)animated{
    self.navigationController.navigationBarHidden=YES;
//    [self retriveAllSchedulePostCount];
    [self retriveAllSchedulePost];
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
    
//    if (([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary] == NO
//         && [UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum] == NO)) {
//        return NO;
//    }
//    
//    UIImagePickerController *cameraUI = [[UIImagePickerController alloc] init];
//    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary]
//        && [[UIImagePickerController availableMediaTypesForSourceType:UIImagePickerControllerSourceTypePhotoLibrary] containsObject:(NSString *)kUTTypeImage]) {
//        
//        cameraUI.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
//        cameraUI.mediaTypes = [NSArray arrayWithObject:(NSString *) kUTTypeImage];
//        
//    } else if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeSavedPhotosAlbum]
//               && [[UIImagePickerController availableMediaTypesForSourceType:UIImagePickerControllerSourceTypeSavedPhotosAlbum] containsObject:(NSString *)kUTTypeImage]) {
//        
//        cameraUI.sourceType = UIImagePickerControllerSourceTypeSavedPhotosAlbum;
//        cameraUI.mediaTypes = [NSArray arrayWithObject:(NSString *) kUTTypeImage];
//        
//    } else {
//        return NO;
//    }
//    
//    cameraUI.allowsEditing = YES;
//    cameraUI.delegate = self;
//    
//    [self presentViewController:cameraUI animated:YES completion:nil];
    
    PostScheduleController *postSchedule=[[PostScheduleController alloc] init];
    postSchedule.type=@"Text";
    postSchedule.title=@"Schedule Post";
    [self.navigationController pushViewController:postSchedule animated:YES];
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
