//
//  AppDelegate.h
//  f-boardpro
//

//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <sqlite3.h>
#import "AppDelegateFboard.h"
#import "MBProgressHUD.h"

@interface AppDelegateFboard : UIResponder <UIApplicationDelegate,UINavigationControllerDelegate>{
    MBProgressHUD *HUD;
    NSString *databasePath;
    sqlite3 *_databaseHandle;
}

@property (strong, nonatomic) UIWindow *window;

@property (nonatomic, strong) NSString *imageCheck;


@property(nonatomic,strong)NSString *accesstokenString;

@property(nonatomic,strong)UINavigationController *navController;
+(AppDelegateFboard *)sharedAppDelegate;
-(void) showHUDLoadingView:(NSString *)strTitle;
-(void) hideHUDLoadingView;
-(void)showToastMessage:(NSString *)message;
@end

