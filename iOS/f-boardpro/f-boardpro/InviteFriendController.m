//
//  InviteFriendController.m
//  f-boardpro
//

//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "InviteFriendController.h"
#import "SUCache.h"
#import "UIImageView+WebCache.h"
#import <FBSDKShareKit/FBSDKShareKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import "SingletonClass.h"
#import "ImageViewCustomCell.h"

@interface InviteFriendController ()
@property(nonatomic,strong)NSMutableArray *mutfriendList;
@property(nonatomic,strong)NSMutableArray *selectedListArray;

@end

@implementation InviteFriendController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor=[UIColor colorWithRed:250.0f/255.0f green:250.0f/255.0f blue:250.0f/255.0f alpha:.8f];
    UICollectionViewFlowLayout *layout=[[UICollectionViewFlowLayout alloc] init];
    [layout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [layout setItemSize:CGSizeMake(150, 50)];
    
    self.listCollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(5,20, self.view.frame.size.width-10,self.view.frame.size.height-175) collectionViewLayout:layout];
    [self.listCollectionView registerClass:[ImageViewCustomCell class] forCellWithReuseIdentifier:@"cell"];
    [self.listCollectionView setBackgroundColor:[UIColor clearColor]];
    self.listCollectionView.delegate = self;
    self.listCollectionView.dataSource = self;
    [self.view addSubview:self.listCollectionView];
    
    UIButton *sendInvitation=[UIButton buttonWithType:UIButtonTypeRoundedRect];
    [sendInvitation addTarget:self action:@selector(inviteRequest:) forControlEvents:UIControlEventTouchUpInside];
    [sendInvitation setBackgroundColor:[UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f]];
    [sendInvitation setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    sendInvitation.layer.borderColor=[[UIColor blackColor] CGColor];
    sendInvitation.layer.borderWidth=2.0f;
    sendInvitation.layer.cornerRadius=5.0f;
    [sendInvitation setTitle:@"Send Invitation" forState:UIControlStateNormal];
    sendInvitation.frame=CGRectMake(self.view.frame.size.width/2-100, self.view.frame.size.height-130, 200, 50);
    [self.view addSubview:sendInvitation];
    
        // Do any additional setup after loading the view.
}

-(void)viewWillAppear:(BOOL)animated{
[[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(inviteFriendList) name:@"CurrentUserChangedNotification" object:nil];
    self.navigationController.navigationBarHidden=YES;
    [self inviteFriendList];
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
    
}


-(void)inviteFriendList{
    self.mutfriendList=[[NSMutableArray alloc] init];
    self.selectedListArray=[[NSMutableArray alloc] init];
    NSIndexPath  *userIndex=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:userIndex.row+userIndex.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
        NSLog(@"%@",[FBSDKAccessToken currentAccessToken].tokenString);
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"/me/taggable_friends" parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
         [self.mutfriendList addObjectsFromArray:(NSArray *) result[@"data"]];
             [self.listCollectionView reloadData];
        }];
        
     }
}

- (void)appInviteDialog:(FBSDKAppInviteDialog *)appInviteDialog didCompleteWithResults:(NSDictionary *)results{
    NSLog(@"result %@",results);

}

- (void)appInviteDialog:(FBSDKAppInviteDialog *)appInviteDialog didFailWithError:(NSError *)error{

    NSLog(@"error %@",error);

}

- (NSInteger)collectionView:(UICollectionView *)view numberOfItemsInSection:(NSInteger)section {
    
        //    return self.friendListArray.count;
    return self.mutfriendList.count;
}
    // 2
-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    NSString * identifier=@"cell";
    
        //    NSDictionary *dict = [self.friendListArray objectAtIndex:indexPath.row];
    NSDictionary *dict = [self.mutfriendList objectAtIndex:indexPath.row];
    NSString *imageUrl = [NSString stringWithFormat:@"%@",[[[dict objectForKey:@"picture"] objectForKey:@"data"] objectForKey:@"url"]];
    
    ImageViewCustomCell * cell=(ImageViewCustomCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
    [cell.imageView setImageWithURL:[NSURL URLWithString:imageUrl] placeholderImage:[UIImage imageNamed:@"icon-58.png"]];
    NSLog(@"%@ name = ",[dict objectForKey:@"name"]);
//    [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f]
      cell.backgroundColor=[UIColor whiteColor];
    self.checkButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.checkButton setImage:[UIImage imageNamed:@"plus_icon.png"] forState:UIControlStateNormal ];
    [self.checkButton setImage:[UIImage imageNamed:@"check_icon.png"] forState:UIControlStateSelected ];
    NSNumber *indexPasth=[NSNumber numberWithInteger:indexPath.row];
    if (![self.selectedListArray containsObject:indexPasth]) {
        [self.checkButton setImage:[UIImage imageNamed:@"plus_icon.png"] forState:UIControlStateNormal ];
        
    }else{
        [self.checkButton setImage:[UIImage imageNamed:@"check_icon.png"] forState:UIControlStateNormal ];
        [self.checkButton setImage:[UIImage imageNamed:@"plus_icon.png"] forState:UIControlStateSelected ];
            //      self.checkButton.selected = YES;
    }
    
    
    self.checkButton.frame = CGRectMake(125.0, 10.0, 20.0, 20.0);
    self.checkButton.tag=indexPath.row;
    self.checkButton.userInteractionEnabled = YES;
    [self.checkButton addTarget:self action:@selector(buttonTouch:withEvent:) forControlEvents:UIControlEventTouchUpInside];
    [cell addSubview:self.checkButton];
    
    NSString* nameStr = [dict objectForKey:@"name"];
    NSArray* firstLastStrings = [nameStr componentsSeparatedByString:@" "];
    NSString* firstName = [firstLastStrings objectAtIndex:0];
    NSString* lastName = [firstLastStrings objectAtIndex:1];
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@\n%@",firstName,lastName];
    cell.layer.cornerRadius=4.0;
    cell.layer.borderWidth=0.5;
    cell.layer.borderColor=[[UIColor blackColor] CGColor];
    cell.textLabel.textColor=[UIColor blackColor];
    
    cell.layer.shadowColor = [UIColor blackColor].CGColor;
    cell.layer.shadowOpacity = 0.4f;
    cell.layer.shadowOffset = CGSizeMake(0.0f, 4.0f);
    cell.layer.shadowRadius = 1.5f;
    cell.layer.masksToBounds = NO;

    return cell;
}

-(void)inviteRequest:(UIButton *)button{
    self.selectedFrndsString = [[NSMutableString alloc] init];
    for (int i =0; i < self.selectedListArray.count; i++) {
        NSNumber *indexPath = [self.selectedListArray objectAtIndex:i];

        NSDictionary *dict = [self.mutfriendList objectAtIndex:[indexPath integerValue]];
        NSString *toString = [NSString stringWithFormat:@"%@",[dict objectForKey:@"id"]];
        
        if (i== self.selectedListArray.count-1) {
            [self.selectedFrndsString appendString:toString];
        }
        else{
            [self.selectedFrndsString appendString:[NSString stringWithFormat:@"%@,",toString]];
        }
    }
    
    NSURL *imageURL = [NSURL URLWithString:@"http://i.imgur.com/GQOn2oe.png"];
    FBSDKSharePhoto *photo = [FBSDKSharePhoto photoWithImageURL:imageURL userGenerated:NO];
    NSDictionary *properties = @{
                                 @"og:type": @"f-boardproapp:request",
                                 @"og:title": @"Installation Request",
                                 @"og:description": @"f-boardpro is a multiple facebook account management app, it helps you to login to multiple facebook accounts from your iOS or Android device and do various facebook activities like post, like, comment,managing pages, groups and much more.",
                                 @"og:url": @"https://itunes.apple.com/app/id991615474",
                                 @"og:image": @[photo]
                                 };
    FBSDKShareOpenGraphObject *object = [FBSDKShareOpenGraphObject objectWithProperties:properties];
    FBSDKShareAPI *shareAPI = [[FBSDKShareAPI alloc] init];
    [shareAPI createOpenGraphObject:object];
    
    FBSDKShareOpenGraphAction *action = [[FBSDKShareOpenGraphAction alloc] init];
    action.actionType = @"f-boardproapp:invite";
   [action setObject:object  forKey:@"request"];
    [action setString:self.selectedFrndsString forKey:@"tags"];
    FBSDKShareOpenGraphContent *content = [[FBSDKShareOpenGraphContent alloc] init];
    content.action = action;
    content.previewPropertyName = @"request";
        // optionally set the delegate
        // shareAPI.delegate = self;
    shareAPI.shareContent = content;
    BOOL isShare  = [shareAPI share];
    if (isShare) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"Invitations sent to your freind" message:@"Successfuly Delivered" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    }
    
    
}

- (void)buttonTouch:(UIButton *)aButton withEvent:(UIEvent *)event
{
    aButton.selected = ! aButton.selected;
    
    NSInteger bu=aButton.tag;
    NSNumber *indexpath=[NSNumber numberWithInteger:bu];
    if (![self.selectedListArray containsObject:indexpath]) {
        [self.selectedListArray addObject:indexpath];
    }else{
        [self.selectedListArray removeObject:indexpath];
    }
    
}


- (BOOL)collectionView:(UICollectionView *)collectionView
      canPerformAction:(SEL)action
    forItemAtIndexPath:(NSIndexPath *)indexPath
            withSender:(id)sender {
    return YES;  // YES for the Cut, copy, paste actions
}

- (BOOL)collectionView:(UICollectionView *)collectionView
shouldShowMenuForItemAtIndexPath:(NSIndexPath *)indexPath {
    return YES;
}

- (void)collectionView:(UICollectionView *)collectionView
         performAction:(SEL)action
    forItemAtIndexPath:(NSIndexPath *)indexPath
            withSender:(id)sender {
    NSLog(@"performAction");
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
