//
//  AppDelegate.m
//  f-boardpro
//

//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "AppDelegate.h"
#import "SingletonClass.h"
#import "SUCache.h"
#import "ScheduledPostController.h"
#import "MYFeed.h"
#import "PAPPhotoTimelineViewController.h"
#import "PageViewController.h"
#import "PostViewController.h"
#import "PageViewController.h"
    //#import "SUAccountsViewController.h"
#import "GroupViewController.h"
#import "FBAutomationViewController.h"
#import "CustomMenuViewController.h"
    //#import "SUAccountsViewController.h"
#import "AccountViewController.h"
#import "ProfileViewController.h"
#import <sqlite3.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    NSInteger account= [[NSUserDefaults standardUserDefaults]integerForKey:@"PrimaryUser"];
        //    [[NSUserDefaults standardUserDefaults]synchronize];
    
    UILocalNotification *localNotif = [launchOptions objectForKey:UIApplicationLaunchOptionsLocalNotificationKey];
    if (localNotif) {
        [self postSchedule:localNotif];
    }
    [self saveinSqlite];
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    if (account) {
        
        MYFeed *follow = [[MYFeed alloc]init];
        follow.title=@"My Feeds";
        
        PAPPhotoTimelineViewController *unfollow=[[PAPPhotoTimelineViewController alloc] initWithStyle:UITableViewStylePlain];
        unfollow.title=@"Home Feeds";
        
        PageViewController *profile = [[PageViewController alloc] init];
        profile.title  = @"Pages";
        
        PostViewController *post = [[PostViewController alloc] init];
        post.title = @"Post schedule";
        
        GroupViewController *grp = [[GroupViewController alloc]init];
        grp.title = @"Group details";
        
        UICollectionViewFlowLayout *layout=[[UICollectionViewFlowLayout alloc] init];
        [layout setScrollDirection:UICollectionViewScrollDirectionVertical];
        [layout setItemSize:CGSizeMake(150, 50)];
        
        
        FBAutomationViewController *automaton=[[FBAutomationViewController alloc] initWithCollectionViewLayout:layout];
        automaton.title=@"Friend List";
        
        ScheduledPostController *schedule=[[ScheduledPostController alloc] init];
        schedule.title=@"Scheduled";
        
        ProfileViewController *profileView=[[ProfileViewController alloc] init];
        profileView.title=@"Profile";
        NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
            //    NSLog(@"index path %d",index.section+index.row);
        FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
        if (token) {
            [FBSDKAccessToken setCurrentAccessToken:token];
        }
     
        profileView.FBid=[FBSDKAccessToken currentAccessToken].userID;
        
        CustomMenuViewController *customMenu=[[CustomMenuViewController alloc] init];
        customMenu.viewControllers = @[follow,unfollow,profile,post,grp,automaton,schedule,profileView];
        
        
        
        self.navController=[[UINavigationController alloc] initWithRootViewController:customMenu];
        
        
        
    }else{
        AccountViewController *accountView=[[AccountViewController alloc] init];
        self.navController=[[UINavigationController alloc] initWithRootViewController:accountView];
        
        
    }
    self.navController.navigationBarHidden=YES;
    self.navController.delegate=self;
    [self.window setRootViewController: self.navController];
    [self.window makeKeyAndVisible];
    [FBSDKProfile enableUpdatesOnAccessTokenChange:YES];
    
    return [[FBSDKApplicationDelegate sharedInstance] application:application
                                    didFinishLaunchingWithOptions:launchOptions];
}

-(void)saveinSqlite
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    
    NSLog(@"%@",paths);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    databasePath = [documentsDirectory stringByAppendingPathComponent:@"ImageDataBase.sqlite"];
    NSFileManager *defaultManager=[NSFileManager defaultManager];
    NSError *error;
    
    
    if (![[NSFileManager defaultManager]fileExistsAtPath:databasePath]) {
        
    
    
        // Open the database and store the handle as a data member
    if (sqlite3_open([databasePath UTF8String], &_databaseHandle) == SQLITE_OK)
    {
            // Create the database if it doesn't yet exists in the file system
        
        
            // Create the PERSON table
        const char *sqlStatement = "CREATE TABLE IF NOT EXISTS IMAGES (URL TEXT , IMAGE BLOB, CDATE TEXT,ATOKEN TEXT )";
        /*NSString *insert=[NSString stringWithFormat:@"insert into person(id,firsrtname) VALUES(\"%@\",\"%@\")",1,"hunny"];*/
        /*  const char insert="INSERT INTO PERSON (FIRSTNAME, LASTNAME, BIRTHDAY) VALUES ('""1'""'hunny'""'singh'""'1992-08-14')";*/
        char *error;
        if (sqlite3_exec(_databaseHandle, sqlStatement, NULL, NULL, &error) == SQLITE_OK)
        {
            NSLog(@"table created");
                // Create the ADDRESS table with foreign key to the PERSON table
            
            NSLog(@"Database and tables created.");
                // [self convertImageToData];
        }
        else
        {
            NSLog(@"````Error: %s", error);
        }
    }
        
    }
}

    // Load images from data base with given image url
- (NSData *) LoadImagesFromSql: (NSString*) imageLink timestamp:(NSString *)stamp
{
    NSData* data = nil;
    NSString* sqliteQuery = [NSString stringWithFormat:@"SELECT IMAGE,ATOKEN FROM IMAGES WHERE URL = '%@' AND CDATE= '%@'", imageLink,stamp];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSLog(@"-------%@",paths);
    sqlite3_stmt *stmt=nil;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    databasePath = [documentsDirectory stringByAppendingPathComponent:@"ImageDataBase.sqlite"];
    
    sqlite3_stmt* statement;
    if(sqlite3_open([databasePath UTF8String], &_databaseHandle)==SQLITE_OK){
        if( sqlite3_prepare_v2(_databaseHandle, [sqliteQuery UTF8String], -1, &statement, NULL) == SQLITE_OK )
        {
            while( sqlite3_step(statement) == SQLITE_ROW )
            {
                int length = sqlite3_column_bytes(statement, 0);
                data       = [NSData dataWithBytes:sqlite3_column_blob(statement, 0) length:length];
                self.accesstokenString = [NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, 1)];
                NSLog(@"name string %@",self.accesstokenString);
                
                    //          [FBSDKAccessToken setCurrentAccessToken:(FBSDKAccessToken *)nameString];
            }
        }else{
            NSLog( @"SaveBody: Failed from sqlite3_prepare_v2. Error is:  %s", sqlite3_errmsg(_databaseHandle) );
        }
    }
        // Finalize and close database.
    sqlite3_finalize(statement);
    sqlite3_close(_databaseHandle);
    
    
    UIImage *image = [UIImage imageWithData:data];
    
        // [self deleteLocalDataBase:imageLink];
    
    return data;
    
}

-(void)deleteLocalDataBase:(NSString *)imageName timeStamp:(NSString *)stamp
{
    NSData* data = nil;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSLog(@"-------%@",paths);
    sqlite3_stmt *stmt=nil;
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *databasePath = [documentsDirectory stringByAppendingPathComponent:@"ImageDataBase.sqlite"];
    
    NSString *query = [NSString stringWithFormat:@"DELETE FROM IMAGES WHERE URL='%@' AND CDATE='%@'", imageName,stamp];
    
    sqlite3_stmt* statement;
    if(sqlite3_open([databasePath UTF8String], &_databaseHandle)==SQLITE_OK){
        if( sqlite3_prepare_v2(_databaseHandle, [query UTF8String], -1, &statement, NULL) == SQLITE_OK )
        {
            if( sqlite3_step(statement) == SQLITE_DONE )
            {
                NSLog(@"success");
            }
        }else{
            NSLog( @"SaveBody: Failed from sqlite3_prepare_v2. Error is:  %s", sqlite3_errmsg(_databaseHandle) );
        }
        sqlite3_finalize(statement);
    }
    sqlite3_close(_databaseHandle);
    
}

-(void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification
{
    [self postSchedule:notification];
}


-(void)postSchedule:(UILocalNotification *)notification{
    
    NSString *message = [notification.userInfo objectForKey:@"Text"];
    NSString *timeStamp = [notification.userInfo objectForKey:@"TimeStamp"];
    NSString *fbId = [notification.userInfo objectForKey:@"UserId"];
    self.imageCheck = [notification.userInfo objectForKey:@"image"];
    
    NSLog(@"message=%@, timestam=%@,fbId=%@,self.imagecheck=%@",message,timeStamp,fbId,self.imageCheck);
    
    NSData *data;
    UIImage *img;
    
    if ([self.imageCheck isEqualToString:@"true"]) {
        data = [self LoadImagesFromSql:fbId timestamp:timeStamp];
        img = [UIImage imageWithData:data];
    }
    
    
        //case 1 : both image and message data are there
    
    if ([self.imageCheck isEqualToString:@"true"] && ![message isEqualToString:@"Reminder to post"]) {
        
        
        img = [UIImage imageWithData:data];
            // Update the UI
        if ([[FBSDKAccessToken currentAccessToken]
             hasGranted:@"publish_actions"]) {
            [[[FBSDKGraphRequest alloc]
              initWithGraphPath:@"me/photos"
              parameters: @{ @"message" : message,@"picture":UIImagePNGRepresentation(img)}
              HTTPMethod:@"POST"]
             startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
                 if (!error) {
                     NSLog(@"Post id:%@", result[@"id"]);
                     [self deleteLocalDataBase:fbId timeStamp:timeStamp];
                     
                 }
             }];
        }
        
    }
        //case 2: image && message = nil
    
    if ([self.imageCheck isEqualToString:@"true"]){
        if([message isEqualToString:@"Reminder to post"]) {
            if ([[FBSDKAccessToken currentAccessToken] hasGranted:@"publish_actions"]) {
                [[[FBSDKGraphRequest alloc]
                  initWithGraphPath:@"me/photos"
                  parameters: @{ @"picture":UIImagePNGRepresentation(img)}
                  HTTPMethod:@"POST"]
                 startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
                     if (!error) {
                         NSLog(@"Post id:%@", result[@"id"]);
                     }
                 }];
            }
            
        }
    }
        //case 3 : image is nil and message is there.
    
    if (![self.imageCheck isEqualToString:@"true"]) {
        if (![message isEqualToString:@"Reminder to post"]) {
            
            if ([[FBSDKAccessToken currentAccessToken] hasGranted:@"publish_actions"]) {
                [[[FBSDKGraphRequest alloc]
                  initWithGraphPath:@"me/feed"
                  parameters: @{ @"message" : message}
                  HTTPMethod:@"POST"]
                 startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
                     if (!error) {
                         NSLog(@"Post id:%@", result[@"id"]);
                     }
                 }];
            }
        }
    }
}


#pragma mark -
#pragma mark - Loading View


+(AppDelegate *)sharedAppDelegate
{
    return (AppDelegate *)[[UIApplication sharedApplication] delegate];
}

-(void) showHUDLoadingView:(NSString *)strTitle
{
    HUD = [[MBProgressHUD alloc] initWithView:self.window];
    [self.window addSubview:HUD];
        //HUD.delegate = self;
        //HUD.labelText = [strTitle isEqualToString:@""] ? @"Loading...":strTitle;
    HUD.detailsLabelText=[strTitle isEqualToString:@""] ? @"Loading...":strTitle;
    [HUD show:YES];
}

-(void) hideHUDLoadingView
{
    [HUD removeFromSuperview];
}

-(void)showToastMessage:(NSString *)message
{
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.window
                                              animated:YES];
    
        // Configure for text only and offset down
    hud.mode = MBProgressHUDModeText;
    hud.detailsLabelText = message;
    hud.margin = 10.f;
    hud.yOffset = 150.f;
    hud.removeFromSuperViewOnHide = YES;
    [hud hide:YES afterDelay:2.0];
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
