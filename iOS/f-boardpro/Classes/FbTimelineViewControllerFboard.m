//
//  PAPPhotoTimelineViewController.m
//  Anypic
//
//  Created by Héctor Ramos on 5/2/12.
//  Copyright (c) 2013 Parse. All rights reserved.
//

#import "FbTimelineViewControllerFboard.h"
#import "MBProgressHUD.h"
#import "AppDelegateFboard.h"
#import "PAPPhotoCellFboard.h"
#import "SUCacheFboard.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import "SingletonClass.h"
#import "UIImageView+WebCache.h"
//#import "PAPAccountViewController.h"
#import "CommentsViewControllerFboard.h"
//#import "PAPUtility.h"
//#import "PAPLoadMoreCell.h"

@interface FbTimelineViewControllerFboard (){
    UILabel *label;
    NSArray *array;
    PAPPhotoCellFboard *cell1;
    UILabel *messageLabel;
}
@property(nonatomic,strong)UIRefreshControl* refreshControl;

@property (nonatomic, assign) BOOL shouldReloadOnAppear;
@property (nonatomic, strong) NSMutableSet *reusableSectionHeaderViews;
@property (nonatomic, strong) NSMutableDictionary *outstandingSectionHeaderQueries;
@end

@implementation FbTimelineViewControllerFboard
@synthesize reusableSectionHeaderViews,refreshControl;
@synthesize shouldReloadOnAppear;
@synthesize outstandingSectionHeaderQueries;

#pragma mark - Initialization

//- (void)dealloc {
//    
//}
//
//- (id)initWithStyle:(UITableViewStyle)style {
//    self = [super initWithStyle:style];
//    if (self) {
//        
//            }
//    return self;
//}


#pragma mark - UIViewController

- (void)viewDidLoad {

    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    [super viewDidLoad];
    
    
    self.refreshControl = [[UIRefreshControl alloc] init];
    [refreshControl addTarget:self action:@selector(refershControlAction) forControlEvents:UIControlEventValueChanged];
     [self.tableView addSubview:refreshControl];
    self.tableView.scrollEnabled=NO;

//    NSArray *itemArray=[NSArray arrayWithObjects:@"Status",@"Photo", nil];
//    self.segmentControl = [[UISegmentedControl alloc] initWithItems:itemArray];
//    self.segmentControl.frame =CGRectMake(-4,5,self.view.frame.size.width,30);
//    [self.segmentControl addTarget:self action:@selector(mySegmentControlAction:) forControlEvents: UIControlEventValueChanged];
//    self.tableView.tableHeaderView=self.segmentControl;
//  [self.segmentControl setTitleTextAttributes:@{NSForegroundColorAttributeName:[UIColor blackColor]} forState:UIControlStateNormal];
//    [self.segmentControl setBackgroundColor:[UIColor whiteColor]];
//    [self.segmentControl setTintColor:[UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f]];

    
//    UIView *texturedBackgroundView = [[UIView alloc] initWithFrame:self.view.bounds];
//    texturedBackgroundView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"bg.png"]];
//    self.tableView.backgroundView = texturedBackgroundView;
//    self.grpIdArray=[[NSMutableArray alloc] init];
//    [self fetchFeeds];
    
    UIImageView *imageView = [[UIImageView alloc]init];
    imageView.frame=CGRectMake(70,40, SCREEN_WIDTH-140, 30);
    
    
    UIImage *image = [UIImage imageNamed:@"fbrandpro.png"];
    [imageView setImage:image];
    [self.view addSubview:imageView];
    
    
    messageLabel =[[UILabel alloc]init];
    messageLabel.frame=CGRectMake(20,80, SCREEN_WIDTH-40,250);
    messageLabel.backgroundColor=[UIColor whiteColor];
    messageLabel.numberOfLines=0;
    messageLabel.lineBreakMode=NSLineBreakByWordWrapping;
    messageLabel.textAlignment=NSTextAlignmentCenter;
    if ([UIScreen mainScreen].bounds.size.width==320)
    {
        
        messageLabel.text=@"In Accordance With recent API Guidelines,\nFacebook has stopped allowing regular users \nto manage group via API\n\nOnly Testers,developers and App admins are authorised so,to become a tester,please send a friend request to the following FB id\n\n\nwith message\n\n\n\n\nFor more updates join SociBoard group on FB";
    }
    else
    {
        messageLabel.text=@"In Accordance With recent API Guidelines,\nFacebook has stopped allowing regular users \nto manage group via API\n\nOnly Testers,developers and App admins are authorised so,to become a tester,please send a friend request to the following FB id\n\n\nwith message\n\n\n\n\n\n\n\n\n\n\n\nFor more updates join SociBoard group on FB";
    }
    
    messageLabel.font=[UIFont boldSystemFontOfSize:12];
    [self.view addSubview:messageLabel];
    
    
    UILabel * label1 = [[UILabel alloc]init];
    
    label1.frame=CGRectMake(20, 300, SCREEN_WIDTH-40, 30);
    label1.backgroundColor=[UIColor clearColor];
    label1.numberOfLines=1;
    label1.textAlignment=NSTextAlignmentCenter;
    label1.font=[UIFont italicSystemFontOfSize:12];
    label1.text = @"\"please add me to Fboardpro app\"";
    [self.view addSubview:label1];
    
    
    UILabel *linkLabel = [[UILabel alloc]init];
    linkLabel.frame=CGRectMake(20, 240, SCREEN_WIDTH-40, 20);
    linkLabel.textColor=ThemeColorFboard;
    linkLabel.numberOfLines=1;
    linkLabel.textAlignment=NSTextAlignmentCenter;
    linkLabel.font=[UIFont italicSystemFontOfSize:12];
    linkLabel.userInteractionEnabled=YES;
    NSDictionary *underlineAttribute = @{NSUnderlineStyleAttributeName: @(NSUnderlineStyleSingle)};
    linkLabel.attributedText = [[NSAttributedString alloc] initWithString:@"https://www.facebook.com/sumit ghosh"
                                                               attributes:underlineAttribute];
    UITapGestureRecognizer * tapOnLinkLbl=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(openSumitSirFbProfile)];
    [linkLabel addGestureRecognizer:tapOnLinkLbl];
    [self.view addSubview:linkLabel];
    
    
    UILabel *linkLabelFbGroup = [[UILabel alloc]init];
    linkLabelFbGroup.frame=CGRectMake(20, 350, SCREEN_WIDTH-40, 20);
    linkLabelFbGroup.textColor=ThemeColorFboard;
    linkLabelFbGroup.numberOfLines=2;
    linkLabelFbGroup.textAlignment=NSTextAlignmentCenter;
    linkLabelFbGroup.font=[UIFont italicSystemFontOfSize:12];
    linkLabelFbGroup.userInteractionEnabled=YES;
    underlineAttribute = @{NSUnderlineStyleAttributeName: @(NSUnderlineStyleSingle)};
    linkLabelFbGroup.attributedText = [[NSAttributedString alloc] initWithString:@"https://www.facebook.com/groups"
                                                                      attributes:underlineAttribute];
    UITapGestureRecognizer * tapOnLinkLblFbGroup=[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(openFbGroup)];
    [linkLabelFbGroup addGestureRecognizer:tapOnLinkLblFbGroup];
    [self.view addSubview:linkLabelFbGroup];
    
}
-(void)openSumitSirFbProfile
{
    NSURL * urlTemp= [NSURL URLWithString:@"https://www.facebook.com/sumit.ghosh?ref=ts&fref=ts"];
    [[UIApplication sharedApplication]openURL:urlTemp];
}
-(void)openFbGroup
{
    NSURL * urlTemp= [NSURL URLWithString:@"https://www.facebook.com/groups/621124567991195/"];
    [[UIApplication sharedApplication]openURL:urlTemp];
  
}



//-(void)mySegmentControlAction:(UISegmentedControl *)segment{
//    
//    if (segment.selectedSegmentIndex==0) {
//        
//        NSDictionary *params = @{
//                                 @"message": @"This is a test message",
//                                 };
//      NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//            //    NSLog(@"index path %ld",index.section+index.row);
//        FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
//        if (token) {
//            
//            [FBSDKAccessToken setCurrentAccessToken:token];
//            FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/feed" parameters:params HTTPMethod:@"POST"];
//            
//            [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//            }];
//       
//    }else{
//        NSDictionary *parameters = @{
//                                 @"message": @"This is a test message",
//                                 };
//        
//        
//        NSIndexPath *indexs=[SingletonClass sharedState].selectedUserIndex;
//            //    NSLog(@"index path %ld",index.section+index.row);
//        FBSDKAccessToken *tokens = [SUCache itemForSlot:indexs.section+indexs.row].token;
//        if (tokens) {
//            
//            [FBSDKAccessToken setCurrentAccessToken:tokens];
//            FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@" me/photos" parameters:parameters HTTPMethod:@"POST"];
//            
//            [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//            }];
//    }
//    }
//    }
//}
//
//-(void)fetchFeeds{
//    
//    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
//    
//    if (!connection) {
//        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
//        [alert show];
//        return ;
//    }
//
//    self.grpIdArray=[[NSMutableArray alloc] init];
//    [[AppDelegate sharedAppDelegate]showHUDLoadingView:@""];
//    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//        //    NSLog(@"index path %ld",index.section+index.row);
//    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
//    if (token) {
//        
//        [FBSDKAccessToken setCurrentAccessToken:token];
//        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"me/home" parameters:nil];
//        
//        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//            
//            [[AppDelegate sharedAppDelegate]hideHUDLoadingView];
//            
//                //  NSLog(@"result is %@",result);
//            
//            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
//                
//                
//                if ([dic objectForKey:@"picture"]) {
//                    [self.grpIdArray addObject:dic];
//
//                }
//            }
//            [self.tableView reloadData];
//        }];
//    }
//
//}
//
//- (void)viewDidAppear:(BOOL)animated {
//    [super viewDidAppear:animated];
//    
//    if (self.shouldReloadOnAppear) {
//        self.shouldReloadOnAppear = NO;
//        
//           }
//}
//
//- (UIStatusBarStyle)preferredStatusBarStyle {
//    return UIStatusBarStyleLightContent;
//}
//
//
//#pragma mark - UITableViewDataSource
//
//- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
//    NSInteger sections = self.grpIdArray.count;
////    if (self.paginationEnabled && sections != 0)
////        sections++;
//    return sections;
//}
//
//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//    return 1;
//}
//
//
//#pragma mark - UITableViewDelegate
//
//- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
//   
//
//    PAPPhotoHeaderView *headerView = [self dequeueReusableSectionHeaderView];
//    
//    if (!headerView) {
//        headerView = [[PAPPhotoHeaderView alloc] initWithFrame:CGRectMake( 0.0f, 0.0f, self.view.bounds.size.width, 44.0f) buttons:PAPPhotoHeaderButtonsDefault];
//      headerView.delegate = self;
//        [self.reusableSectionHeaderViews addObject:headerView];
//    }
//    
//    NSDictionary *attributesForPhoto = [self.grpIdArray objectAtIndex:section];
////    [headerView setPhoto:photo];
//    headerView.tag = section;
//    [headerView.likeButton setTag:section];
//    
////    NSDictionary *attributesForPhoto = [[PAPCache sharedCache] attributesForPhoto:photo];
//
//    if (attributesForPhoto) {
//        [headerView setLikeStatus:YES];
//        NSString *str2=[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",[[attributesForPhoto objectForKey:@"from"] objectForKey:@"id"]];
//        
//        NSArray *arr3=[[attributesForPhoto objectForKey:@"likes"] objectForKey:@"data"];
//         NSArray *arr2=[[attributesForPhoto objectForKey:@"comments"] objectForKey:@"data"];
////        likeCount
//        [headerView.avatarImageView setImageWithURL:[NSURL URLWithString:str2] placeholderImage:[UIImage imageNamed:@""]];
////        headerView.timestampLabel.text=[attributesForPhoto objectForKey:@"created_time"];
//       headerView.timestampLabel.text= [self dateDiff:[attributesForPhoto objectForKey:@"created_time"]];
//        [headerView.userButton setTitle:[[attributesForPhoto objectForKey:@"from"] objectForKey:@"name"] forState:UIControlStateNormal];
//      
////        headerView.userButton.titleLabel.text=@"Khomes";
//        [[headerView.userButton titleLabel] setTextAlignment:NSTextAlignmentLeft];
//          [headerView setPhoto:attributesForPhoto];
//        [headerView.likeButton setTitle:[NSString stringWithFormat:@"%lu",(unsigned long)arr3.count] forState:UIControlStateNormal];
//        //comment count
//        [headerView.commentButton setTitle:[NSString stringWithFormat:@"%lu",(unsigned long)arr2.count] forState:UIControlStateNormal];
//        
//        if (headerView.likeButton.alpha < 1.0f || headerView.commentButton.alpha < 1.0f||headerView.userButton.alpha < 1.0f) {
//            [UIView animateWithDuration:0.200f animations:^{
//                headerView.likeButton.alpha = 1.0f;
//                headerView.commentButton.alpha = 1.0f;
//                headerView.userButton.alpha=1.0f;
//            }];
//        }
//    } else {
//        headerView.likeButton.alpha = 0.0f;
//        headerView.commentButton.alpha = 0.0f;
//        
//        @synchronized(self) {
//            // check if we can update the cache
//            NSNumber *outstandingSectionHeaderQueryStatus = [self.outstandingSectionHeaderQueries objectForKey:@(section)];
//            if (!outstandingSectionHeaderQueryStatus) {
//                
//                
//                        [headerView setLikeStatus:YES];
//                        [headerView.likeButton setTitle:@"11" forState:UIControlStateNormal];
//                        [headerView.commentButton setTitle:@"12"forState:UIControlStateNormal];
//                        
//                        if (headerView.likeButton.alpha < 1.0f || headerView.commentButton.alpha < 1.0f) {
//                            [UIView animateWithDuration:0.200f animations:^{
//                                headerView.likeButton.alpha = 1.0f;
//                                headerView.commentButton.alpha = 1.0f;
//                            }];
//                        }
//                    }
//         
//        }
//    }
//    
//    return headerView;
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
//    return 44.0f;
//}
//
//- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
//    UIView *footerView = [[UIView alloc] initWithFrame:CGRectMake( 0.0f, 0.0f, self.tableView.bounds.size.width, 16.0f)];
//    return footerView;
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
//    if (section == self.grpIdArray.count) {
//        return 0.0f;
//    }
//    return 16.0f;
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
//
//    
//    if (indexPath.section >= self.grpIdArray.count) {
//        // Load More Section
//        return 44.0f;
//    }
//     NSDictionary *dic= [self.grpIdArray objectAtIndex:indexPath.section];
//    if ([dic objectForKey:@"picture"]) {
//        
//        return 280.0f;
//
//    }else{
//        NSString *str=[dic objectForKey:@"description"];
//        if (str==nil) {
//            str=[dic objectForKey:@"message"];
//        }
//        if (str==nil) {
//            str=[dic objectForKey:@"story"];
//        }
//        if (str!=nil) {
//            
//            NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:str];
//                // Add Background Color for Smooth rendering
//            [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
//                // Add Main Font Color
//            [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
//                // Add paragraph style
//            NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
//            [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
//            [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
//                // Add Font
//            [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:17]} range:NSMakeRange(0, attributedString.length)];
//                // And finally set the text on the label to use this
//                //    label.attributedText = attributedString;
//            
//                // Phew. Now let's make the Bounding Rect
//            CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(tableView.frame.size.width-40, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
//        
//                 return expectedLabelSize.size.height+20;
//             
//             
////            return 150;
//        
//        }else{
//            return 20;
//        }
//    }
//}
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    
//    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//        //    NSLog(@"index path %ld",index.section+index.row);
//    NSDictionary *dic =[self.grpIdArray objectAtIndex:indexPath.section];
//    NSString *str5 = [dic objectForKey:@"id"];
//    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
//    
//    if (token) {
//        
//        [FBSDKAccessToken setCurrentAccessToken:token];
//        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",str5] parameters:nil];
//        
//        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
//            
//            if (result) {
//                PAPPhotoDetailsViewController *photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
//                self.navigationController.navigationBarHidden=NO;
//                [self.navigationController pushViewController:photoDetail animated:YES];
//
//            }
//            
//            
//        }];
//        
//    }
//
//    
//}
//
//-(NSString *)dateDiff:(NSString *)origDate {
//    
//    NSDateFormatter *df = [[NSDateFormatter alloc] init];
//    [df setFormatterBehavior:NSDateFormatterBehavior10_4];
//    [df setDateFormat:@"yyyy'-'MM'-'dd'T'HH':'mm':'ssZZZZ"];
//    NSDate *convertedDate = [df dateFromString:origDate];
//    NSDate *todayDate = [NSDate date];
//    double ti = [convertedDate timeIntervalSinceDate:todayDate];
//    ti = ti * -1;
//    if(ti < 1) {
//        return @"never";
//    } else 	if (ti < 60) {
//        return @"less than a minute ago";
//    } else if (ti < 3600) {
//        int diff = round(ti / 60);
//        return [NSString stringWithFormat:@"%d minutes ago", diff];
//    } else if (ti < 86400) {
//        int diff = round(ti / 60 / 60);
//        return[NSString stringWithFormat:@"%d hours ago", diff];
//    } else if (ti < 2629743) {
//        int diff = round(ti / 60 / 60 / 24);
//        return[NSString stringWithFormat:@"%d days ago", diff];
//    } else {
//        return @"never";
//    }
//}
//
//
//#pragma mark - PFQueryTableViewController
//
//
//
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
//    static NSString *CellIdentifier ;
//     NSDictionary *dic= [self.grpIdArray objectAtIndex:indexPath.section];
//    
//    if ([dic objectForKey:@"picture"]) {
//        CellIdentifier=@"PhotoCell" ;
//    
//    }
////    else{
////        CellIdentifier=@"TextCell" ;
////
////    }
//    
//      PAPPhotoCell * cell = (PAPPhotoCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
//        if (cell == nil) {
//            cell = [[PAPPhotoCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
//            cell.tag=indexPath.section;
//            [cell.photoButton addTarget:self action:@selector(didTapOnPhotoAction:) forControlEvents:UIControlEventTouchUpInside];
//        }
//        
//        cell.photoButton.tag = indexPath.section;
//    if ([dic objectForKey:@"picture"]) {
//
//        [cell.imageView setImageWithURL:[NSURL URLWithString:[dic objectForKey:@"picture"]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
//        
//       
//    }
////    else{
////        
////        cell.description.text=@"Hello";
//////     cell.description.text=   [dic objectForKey:@"description"];
////        NSString *str=[dic objectForKey:@"description"];
////        if (str==nil) {
////            str=[dic objectForKey:@"message"];
////        }
////        if (str==nil) {
////            str=[dic objectForKey:@"story"];
////        }
////        if (str!=nil) {
////            cell.description.text=str;
////        
////            
////             NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithString:str];
////                // Add Background Color for Smooth rendering
////            [attributedString setAttributes:@{NSBackgroundColorAttributeName:[UIColor blackColor]} range:NSMakeRange(0, attributedString.length)];
////                // Add Main Font Color
////            [attributedString setAttributes:@{NSForegroundColorAttributeName:[UIColor colorWithWhite:0.23 alpha:1.0]} range:NSMakeRange(0, attributedString.length)];
////                // Add paragraph style
////            NSMutableParagraphStyle *paragraphStyle = [[NSMutableParagraphStyle alloc] init];
////            [paragraphStyle setLineBreakMode:NSLineBreakByWordWrapping];
////            [attributedString setAttributes:@{NSParagraphStyleAttributeName:paragraphStyle} range:NSMakeRange(0, attributedString.length)];
////                // Add Font
////            [attributedString setAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:5.0]} range:NSMakeRange(0, attributedString.length)];
////                // And finally set the text on the label to use this
////                //    label.attributedText = attributedString;
////            
////                // Phew. Now let's make the Bounding Rect
////            CGRect expectedLabelSize= [attributedString boundingRectWithSize:CGSizeMake(tableView.frame.size.width-40, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin context:nil];
////        
////
////            expectedLabelSize.origin.x=cell.description.frame.origin.x;
////            expectedLabelSize.origin.y=cell.description.frame.origin.y;
////            cell.description.frame=expectedLabelSize;
////            
//////          [cell.description sizeToFit];
////        }
////    }
//     return cell;
//
//}
//
//
//-(void)tagButton:(UIButton *)sender{
//    NSLog(@"tagButton");
// }
//
//#pragma mark - PAPPhotoTimelineViewController
//
//- (PAPPhotoHeaderView *)dequeueReusableSectionHeaderView {
//    for (PAPPhotoHeaderView *sectionHeaderView in self.reusableSectionHeaderViews) {
//        if (!sectionHeaderView.superview) {
//            // we found a section header that is no longer visible
//            return sectionHeaderView;
//        }
//    }
//    
//    return nil;
//}
//
//
//#pragma mark - PAPPhotoHeaderViewDelegate
//
//- (void)photoHeaderView:(PAPPhotoHeaderView *)photoHeaderView didTapUserButton:(UIButton *)button user:(NSString *)user {
//   
//}
//
//- (void)photoHeaderView:(PAPPhotoHeaderView *)photoHeaderView didTapLikePhotoButton:(UIButton *)button photo:(NSString *)photo {
//	// Disable the button so users cannot send duplicate requests
//    [photoHeaderView shouldEnableLikeButton:NO];
//    
//    BOOL liked = !button.selected;
//    [photoHeaderView setLikeStatus:liked];
//    
//    NSString *originalButtonTitle = button.titleLabel.text;
//    
//    NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
//    [numberFormatter setLocale:[[NSLocale alloc] initWithLocaleIdentifier:@"en_US"]];
//    
//    NSNumber *likeCount = [numberFormatter numberFromString:button.titleLabel.text];
//    if (liked) {
//        likeCount = [NSNumber numberWithInt:[likeCount intValue] + 1];
////        [[PAPCache sharedCache] incrementLikerCountForPhoto:photo];
//    } else {
//        if ([likeCount intValue] > 0) {
//            likeCount = [NSNumber numberWithInt:[likeCount intValue] - 1];
//        }
////        [[PAPCache sharedCache] decrementLikerCountForPhoto:photo];
//    }
//    
////    [[PAPCache sharedCache] setPhotoIsLikedByCurrentUser:photo liked:liked];
//    
//    [button setTitle:[numberFormatter stringFromNumber:likeCount] forState:UIControlStateNormal];
//    
//    if (liked) {
//        
//    } else {
//        
//    }
//}
//
//-(void) tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    
//    NSLog(@"index is %ld",(long)indexPath.row);
////    NSLog(@"self.feeds array count is %lu",(unsigned long)self.pictureArray.count);
//    
//    if (indexPath.row == self.grpIdArray.count-1) {
//           [self retrieveMoreObjects];
//    }
//    
//}
//
//
//-(void) retrieveMoreObjects{
//    NSError * error=nil;
//    NSURLResponse * urlReponse=nil;
//    
////    NSString *accessToken1=[[NSUserDefaults standardUserDefaults]objectForKey:@"accessToken"];
//    
//    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//    FBSDKAccessToken *token = [SUCache itemForSlot:index.row+index.section].token;
//    if (token) {
//        [FBSDKAccessToken setCurrentAccessToken:token];
//    }
////    NSString *str1 =self.pagingString;
////    
////    NSString * urlStr=str1;
////    
////    NSString * urlStr2=[urlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
////    
////    NSURL *url=[NSURL URLWithString:urlStr2];
////    
////    NSMutableURLRequest * request=[[NSMutableURLRequest alloc]initWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:50];
////    
////    
////    NSData * data=[ NSURLConnection sendSynchronousRequest:request returningResponse:&urlReponse error:&error];
////    
////    
////    if (data==nil) {
////        NSLog(@" no data");
////        return;
////    }
////    id response=[NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:&error];
////    NSLog(@"reponse is %@",response);
////    
////    self.pagingString = [[response objectForKey:@"paging"]objectForKey:@"next"];
////    NSString *pageStr = [[response objectForKey:@"paging"]objectForKey:@"previous"];
////    if (self.pagingString) {
////            // NSLog(@"self.pagestr is %@",self.pagingString);
////    }
////    
////    
////    for (NSDictionary *dic in [response objectForKey:@"data"] ) {
////        NSString *str = [dic objectForKey:@"picture"];
////        NSString *str2 = [dic objectForKey:@"story"];
////        NSString *str3 = [dic objectForKey:@"description"];
////        NSString *str5 = [dic objectForKey:@"id"];
////        NSString *str4 = [dic objectForKey:@"like_count"];
////        
////        if (str) {
////            [self.pictureArray addObject:str1];
////        }else{
////            [self.pictureArray addObject:@"nil"];
////        }
////        
////        if (str5) {
////            [self.grpIdArray addObject:str5];
////        }else{
////            [self.grpIdArray addObject:@"nil"];
////        }
////        
////        
////        if (str2) {
////            [self.storyArray addObject:str2];
////        }else{
////            [self.storyArray addObject:@"nil"];
////        }
////        
////        
////        if (str3) {
////            [self.descArray addObject:str3];
////        }else{
////            [self.descArray addObject:@"nil"];
////        }
////        
////        if (str4) {
////            [self.likeArray addObject:str4];
////        }else{
////            [self.likeArray addObject:@"nil"];
////        }
////        
////        
////    }
////        //    NSLog(@"self.feeds array is %@",self.feedsArray);
////    [homeFeedTableView reloadData];
//}
//
//
//
//- (void)photoHeaderView:(PAPPhotoHeaderView *)photoHeaderView didTapCommentOnPhotoButton:(UIButton *)button  photo:(NSDictionary *)photo {
//    PAPPhotoDetailsViewController *photoDetailsVC = [[PAPPhotoDetailsViewController alloc] initWithDict:photo];
//    [self.navigationController pushViewController:photoDetailsVC animated:YES];
//}
//
//- (void)photoHeaderView:(PAPPhotoHeaderView *)photoHeaderView didTapTagButton:(UIButton *)button photo:(NSString *)photo{
//   }
//
//#pragma mark - ()
//
//
//- (void)userDidLikeOrUnlikePhoto:(NSNotification *)note {
//    [self.tableView beginUpdates];
//    [self.tableView endUpdates];
//}
//
//- (void)userDidCommentOnPhoto:(NSNotification *)note {
//    [self.tableView beginUpdates];
//    [self.tableView endUpdates];
//}
//
//- (void)userDidDeletePhoto:(NSNotification *)note {
//    // refresh timeline after a delay
//   
//}
//
//- (void)userDidPublishPhoto:(NSNotification *)note {
//    
//}
//
//- (void)userFollowingChanged:(NSNotification *)note {
//    NSLog(@"User following changed.");
//    self.shouldReloadOnAppear = YES;
//}
//
//- (void)didTapOnPhotoAction:(UIButton *)sender {
//    NSIndexPath *index=[SingletonClass sharedState].selectedUserIndex;
//        //    NSLog(@"index path %ld",index.section+index.row);
//    NSDictionary *dic =[self.grpIdArray objectAtIndex:sender.tag];
//    NSString *str5 = [dic objectForKey:@"id"];
//    FBSDKAccessToken *token = [SUCache itemForSlot:index.section+index.row].token;
//    
//    if (token) {
//        
//        [FBSDKAccessToken setCurrentAccessToken:token];
//        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@",str5] parameters:nil];
//        
//        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
//            
//               if (result) {
//            PAPPhotoDetailsViewController *photoDetail=[[PAPPhotoDetailsViewController alloc] initWithDict:result];
//            self.navigationController.navigationBarHidden=NO;
//            [self.navigationController pushViewController:photoDetail animated:YES];
//               }
//            
//        }];
//        
//    }
//    
//  }
//
//-(void)viewWillAppear:(BOOL)animated{
//    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(fetchFeeds) name:@"CurrentUserChangedNotification" object:nil];
//    self.navigationController.navigationBarHidden=YES;
//    [self fetchFeeds];
//}
//
//-(void)viewDidDisappear:(BOOL)animated{
//    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
//
//}
//
//
//- (void)refershControlAction
//{
//     NSLog(@"Reload grid");
//    
//    // The user just pulled down the collection view. Start loading data.
////    [self queryForTable];
//    [self fetchFeeds];
//    [refreshControl endRefreshing];
//    
//}
//
@end