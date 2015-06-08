//
//  DisplayGroupViewController.m
//  TwitterBoard
//
//  Created by GBS-ios on 4/23/15.
//  Copyright (c) 2015 socioboard. All rights reserved.
//

#import "DisplayGroupViewController.h"
#import <FacebookSDK/FacebookSDK.h>
#import "UIImageView+WebCache.h"
#import <FBSDKShareKit/FBSDKShareKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FacebookSDK/FacebookSDK.h>
#import "SUCache.h"
#import "SingletonClass.h"
#import "TableCustomCell.h"

@interface DisplayGroupViewController ()

@end

@implementation DisplayGroupViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor colorWithRed:26.0f/255.0f green:160.0f/255.0f blue:214.0f/255.0f alpha:1.0f];
    
    self.msgArray = [[NSMutableArray alloc]init];
    self.pictureArray = [[NSMutableArray alloc]init];
    self.fromIdArray = [[NSMutableArray alloc]init];
    self.fromNameArray=[[NSMutableArray alloc]init];
    
    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
    
    UIView *view= [[UIView alloc]init];
     view.backgroundColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    view.frame = CGRectMake(0, 0, self.view.frame.size.width,40);
//    [self.view addSubview:view];
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [button addTarget:self
               action:@selector(aMethod)
     forControlEvents:UIControlEventTouchUpInside];
    [button setTitle:@"Back" forState:UIControlStateNormal];
    [button setTitleColor:[UIColor blackColor] forState:
     UIControlStateNormal];
    button.frame = CGRectMake(0, 10.0, 160.0, 40.0);
//    [view addSubview:button];
    
    self.groupView=[[UIImageView alloc]init];
    self.groupView.frame=CGRectMake(10, 80, 50, 50);
    self.groupView.layer.cornerRadius=20;
    [self.groupView setImageWithURL:[NSURL URLWithString:self.iconUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];

    [self.view addSubview:self.groupView];
   

    self.descriptionLabel = [[UILabel alloc]init];
    self.descriptionLabel.text=[NSString stringWithFormat:@"Description = %@",self.desc];
    self.descriptionLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
    self.descriptionLabel.frame=CGRectMake(10, 140, 300, 30);
//    [self.view addSubview:self.descriptionLabel];
    
    self.emailLabel = [[UILabel alloc]init];
    self.emailLabel.text=[NSString stringWithFormat:@"Email of group = %@",self.email];

    self.emailLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
    self.emailLabel.frame=CGRectMake(10, 175, 350, 30);
//    [self.view addSubview:self.emailLabel];
    
    self.ownerNameLabel = [[UILabel alloc]init];
    self.ownerNameLabel.text=[NSString stringWithFormat:@"Group owner = %@",self.ownerName];

    self.ownerNameLabel.font=[UIFont fontWithName:@"HelveticaNeue" size:12];
    self.ownerNameLabel.frame=CGRectMake(10, 210, 300, 30);
//    [self.view addSubview:self.ownerNameLabel];
    [self createFollowTable];

    [self fetchFeeds];
  
    // Do any additional setup after loading the view.
}
-(void)createFollowTable
{
    groupFeedsView=[[UITableView alloc]initWithFrame:CGRectMake(0, 60, self.view.frame.size.width,self.view.frame.size.height-60) style:UITableViewStylePlain];
    groupFeedsView.dataSource=self;
    groupFeedsView.delegate=self;
    groupFeedsView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:groupFeedsView];
}
-(void)fetchFeeds{
    
    BOOL connection= [[NSUserDefaults standardUserDefaults] boolForKey:@"ConnectionAvilable"];
    
    if (!connection) {
        UIAlertView *alert=[[UIAlertView alloc] initWithTitle:@"No internet Connection" message:@"check your internet" delegate:self cancelButtonTitle:@"Ok" otherButtonTitles:nil, nil];
        [alert show];
        return ;
    }
    

    
    NSString *str1 =self.idStr;

    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:[NSString stringWithFormat:@"/%@/feed",str1] parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error3) {
            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"picture"];
                NSString *str2 = [dic objectForKey:@"message"];
                NSString *str3= [[dic objectForKey:@"from"]objectForKey:@"id"];
                NSString *str4= [[dic objectForKey:@"from"]objectForKey:@"name"];

                if (str1) {
                    [self.pictureArray addObject:str1];
                }else{
                    [self.pictureArray addObject:@"nil"];
                }
                
                if (str2) {
                    [self.msgArray addObject:str2];
                }else{
                    [self.msgArray addObject:@"nil"];
                }
                
                
                if (str3) {
                    [self.fromIdArray addObject:str3];
                }else{
                    [self.fromIdArray addObject:@"nil"];
                }
                
                if (str4) {
                    [self.fromNameArray addObject:str4];
                }else{
                    [self.fromNameArray addObject:@"nil"];
                }

            }
            [groupFeedsView reloadData];
         }];
    }
}


-(void)aMethod{
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display1";
    
    TableCustomCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[TableCustomCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    //    cell.backgroundColor= [UIColor redColor];
    
    
    NSString *str1 =[self.msgArray objectAtIndex:indexPath.row];
   
    if (![str1 isEqualToString:@"nil"]) {
        cell.userNameDesc.text=str1;
    }else{
        cell.userNameDesc.text=@"nothing";
    }
    cell.likeCountLabel.text = @"";
    cell.commentCountLabel.text=@"";
    NSString *picUrl =[self.pictureArray objectAtIndex:indexPath.row];
    
    
    if (![picUrl isEqualToString:@"nil"]) {
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.userImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromIdImage = [self.fromIdArray objectAtIndex:indexPath.row];
    
    if (![fromIdImage isEqualToString:@"nil"]) {
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=small",fromIdImage]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }else{
        [cell.fromUserImage setImageWithURL:[NSURL URLWithString:picUrl] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
    }
    
    NSString *fromLabel1 = [self.fromNameArray objectAtIndex:indexPath.row ];
    cell.fromLabel.text = fromLabel1;
    

    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
}

-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return 1;
}
-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.pictureArray.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 230;
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
