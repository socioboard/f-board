// Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
//
// You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
// copy, modify, and distribute this software in source code or binary form for use
// in connection with the web services and APIs provided by Facebook.
//
// As with any software that integrates with the Facebook platform, your use of
// this software is subject to the Facebook Developer Principles and Policies
// [http://developers.facebook.com/policy/]. This copyright notice shall be
// included in all copies or substantial portions of the software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

#import "SUAccountsViewController.h"
#import "CustomMenuViewController.h"
#import "SingletonClass.h"
#import "UIButton+WebCache.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import <QuartzCore/QuartzCore.h>
#import "SUCache.h"
#import "AppDelegate.h"
#import "SUProfileTableViewCell.h"
#import "ProfileTableViewCell.h"
#import "SUAccountsViewController.h"
#import "MYFeed.h"
#import "PageViewController.h"
#import "PostScheduleController.h"
#import "PAPPhotoTimelineViewController.h"
#import "PostViewController.h"
#import "GroupViewController.h"
#import "FBAutomationViewController.h"


@implementation SUAccountsViewController
{
    NSIndexPath *_currentIndexPath;
}
-(void)viewWillAppear:(BOOL)animated{
    self.navigationController.navigationBarHidden=YES;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_accessTokenChange:) name:FBSDKAccessTokenDidChangeNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_currentProfileChange:) name:FBSDKProfileDidChangeNotification
                                               object:nil];


    self.view.backgroundColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"main_view.png"]];
    UIButton *createAccount = [UIButton buttonWithType:UIButtonTypeCustom];
    createAccount.frame = CGRectMake(60, self.view.frame.size.height/2, 200, 35);
    createAccount.titleLabel.font = [UIFont systemFontOfSize:9.0f];
    [createAccount setImage:[UIImage imageNamed:@"facebookButton.png"] forState:UIControlStateNormal];
    createAccount.titleLabel.shadowOffset = CGSizeMake(0.0f, 0.0f);
    
        //self.menuButton.titleLabel.layer.
    [createAccount addTarget:self action:@selector(createNewAccount) forControlEvents:UIControlEventTouchUpInside];
    [createAccount setTitle:@"CreateAccount"  forState:UIControlStateNormal];
    [self.view addSubview:createAccount];
    

        
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_accessTokenChange:) name:FBSDKAccessTokenDidChangeNotification
                                                       object:nil];
    
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_currentProfileChange:) name:FBSDKProfileDidChangeNotification
                                                       object:nil];

}



-(void)createNewAccount{
    
//    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_accessTokenChange:) name:FBSDKAccessTokenDidChangeNotification
//                                               object:nil];
//    
//    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(_currentProfileChange:) name:FBSDKProfileDidChangeNotification
//                                               object:nil];

    NSArray *permissions =  [[NSArray alloc] initWithObjects:@"publish_actions", nil];
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
//    login.loginBehavior = FBSDKLoginBehaviorWeb;
    
    [login logInWithPublishPermissions:permissions handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
        if (result) {
            NSLog(@"Login permission granted");
        }
        if (!error) {
            
            NSLog(@"result grand permission - %@  declinedPermissions - %@",result.grantedPermissions,result.declinedPermissions);
            [[NSUserDefaults standardUserDefaults]setInteger:1 forKey:@"PrimaryUser"];
            [[NSUserDefaults standardUserDefaults]synchronize];
            
            [[NSNotificationCenter defaultCenter] removeObserver:self];
            
            MYFeed *follow = [[MYFeed alloc]init];
            follow.title=@"My Feeds";
            NSLog(@"Title =- %@",follow.title);
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
            
            CustomMenuViewController *customMenu=[[CustomMenuViewController alloc] init];
              customMenu.viewControllers = @[follow,unfollow,profile,post,grp,automaton];
            self.navigationController.navigationBarHidden=YES;
            [self.navigationController pushViewController:customMenu animated:YES];
        }
        
    
    }];
}

- (void)dealloc
{
//    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (NSInteger)_userSlotFromIndexPath:(NSIndexPath *)indexPath
{
    // Since section 0 has 1 row, we can use this cheap trick
    // so that the "Primary User" cell is slot 0 and the rest
    // follow.
//    return indexPath.row + indexPath.section;
    
    return 0;
}

// Observe a new token, so save it to our SUCache and update
// the cell.
- (void)_accessTokenChange:(NSNotification *)notification
{
    FBSDKAccessToken *token = notification.userInfo[FBSDKAccessTokenChangeNewKey];

    if (!token) {
        [self _deselectRow];
    } else {

      NSInteger slot =0;
        NSLog(@"Accounts Token change %ld",(long)slot);
        SUCacheItem *item = [SUCache itemForSlot:slot] ?: [[SUCacheItem alloc] init];
        if (![item.token isEqualToAccessToken:token]) {
            item.token = token;
            [SUCache saveItem:item slot:slot];

        }
    }
}

// The profile information has changed, update the cell and cache.
- (void)_currentProfileChange:(NSNotification *)notification
{
    NSInteger slot = 0;
 NSLog(@"profile  change %ld",(long)slot);
    FBSDKProfile *profile = notification.userInfo[FBSDKProfileChangeNewKey];
    if (profile) {
        SUCacheItem *cacheItem = [SUCache itemForSlot:slot];
        cacheItem.profile = profile;
        [SUCache saveItem:cacheItem slot:slot];

    }
}

- (void)_deselectRow
{
//    [self.tableView cellForRowAtIndexPath:_currentIndexPath].accessoryType = UITableViewCellAccessoryNone;
//    _currentIndexPath = nil;
    [FBSDKAccessToken setCurrentAccessToken:nil];
    [FBSDKProfile setCurrentProfile:nil];
}

#pragma mark - UITableViewDataSource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 2;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return (section == 0 ? @"Primary User:" : @"Guest Users:");
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return (section == 0 ? 1 :10);
}

- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    return ([SUCache itemForSlot:[self _userSlotFromIndexPath:indexPath]] != nil);
}

- (void)tableView:(UITableView *)tableView
commitEditingStyle:(UITableViewCellEditingStyle)editingStyle
forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        NSInteger slot = [self _userSlotFromIndexPath:indexPath];
        [SUCache deleteItemInSlot:slot];
        SUProfileTableViewCell *cell = (SUProfileTableViewCell *)[tableView cellForRowAtIndexPath:indexPath];
        cell.userName = @"Add New User";
        cell.userID = nil;
        cell.accessoryType = UITableViewCellAccessoryNone;
        if ([_currentIndexPath compare:indexPath] == NSOrderedSame) {
            [self _deselectRow];
        }
        [tableView reloadData];
    }
}

#pragma mark - UITableViewDelegate methods

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    SUProfileTableViewCell *cell = (SUProfileTableViewCell *)[tableView
                                                              dequeueReusableCellWithIdentifier:@"SUProfileTableViewCell"
                                                              forIndexPath:indexPath];
    
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    NSInteger slot = [self _userSlotFromIndexPath:indexPath];
    SUCacheItem *item = [SUCache itemForSlot:slot];
    cell.userName = item.profile.name ?: @"Empty slot";
    cell.userID = item.token.userID;
    if ([item.token isEqualToAccessToken:[FBSDKAccessToken currentAccessToken]]) {
        _currentIndexPath = indexPath;
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [self _deselectRow];
    _currentIndexPath = indexPath;
    NSInteger slot = [self _userSlotFromIndexPath:indexPath];
    FBSDKAccessToken *token = [SUCache itemForSlot:slot].token;
    if (token) {
        // We have a saved token, issue a request to make sure it's still valid.
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/home" parameters:nil];
        
        if (indexPath.section == 1) {
            // Disable the error recovery for the slots that require the webview login,
            // since error recovery uses FBSDKLoginBehaviorNative
            [request setGraphErrorRecoveryDisabled:YES];
        }
    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            // Since we're only requesting /me, we make a simplifying assumption that any error
            // means the token is bad.
            NSError *err;
        
        if (error==nil) {
       
   NSData *data=[NSJSONSerialization dataWithJSONObject:result options:NSJSONWritingPrettyPrinted error:&err];
            
NSDictionary *dict= [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&err];
            
            NSArray *arr=[dict valueForKey:@"data"];
            
    [[NSUserDefaults standardUserDefaults]setObject:arr forKey:@"ValueArray"];
            
//FeedViewController *feed=[[FeedViewController alloc] init];
//    [self.navigationController pushViewController:feed animated:YES];
            CustomMenuViewController *customMenu=[[CustomMenuViewController alloc] init];
            self.navigationController.navigationBar.hidden=YES;
            [self.navigationController pushViewController:customMenu animated:YES];
            
    for( NSDictionary *dictValue in arr) {
        NSLog(@"-----------------dictValue %@",dictValue);
    }
        }
            if (error) {
                [[[UIAlertView alloc] initWithTitle:nil
                                            message:@"The user token is no longer valid."
                                           delegate:nil
                                  cancelButtonTitle:@"OK"
                                  otherButtonTitles:nil] show];
                [SUCache deleteItemInSlot:slot];
                [self _deselectRow];
            }
             NSLog(@"%@",request.description);
            NSLog(@"%@",request.description);
        }];
    } else {
        FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
        if (indexPath.section == 1) {
            login.loginBehavior = FBSDKLoginBehaviorWeb;
        }
        SUProfileTableViewCell *cell = (SUProfileTableViewCell *)[tableView cellForRowAtIndexPath:indexPath];
        cell.userName = @"Loading ...";
        
        
        
     
//        NSSet *set = [NSSet setWithObject:@"read_stream"];
 NSArray *permissions =  [[NSArray alloc] initWithObjects:@"read_stream", nil];

        [login logInWithReadPermissions:permissions handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
            
            NSLog(@"result grand permission - %@  declinedPermissions - %@",result.grantedPermissions,result.declinedPermissions);
            if (error || result.isCancelled) {
                cell.userName = @"Add New User";
                [self _deselectRow];
            }
        }];
    }
    
    
//    FBRequest *friendRequest = [FBRequest requestForGraphPath:@"me/feed"];
//    
//    [friendRequest startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
//        
//        
//    }];

}

- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath {
    return @"Forget";
}

@end
